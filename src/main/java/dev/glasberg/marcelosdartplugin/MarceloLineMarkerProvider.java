package dev.glasberg.marcelosdartplugin;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.SeparatorPlacement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

import static com.jetbrains.lang.dart.DartTokenTypesSets.SINGLE_LINE_COMMENT;

/// See: https://plugins.jetbrains.com/docs/intellij/line-marker-provider.html
public class MarceloLineMarkerProvider implements LineMarkerProvider {

    // import com.intellij.openapi.diagnostic.Logger;
    private static final Logger LOG = Logger.getInstance(MarceloLineMarkerProvider.class);

    enum CallExpressionType {
        TEST,
        TEST_GROUP,
    }

    private MarceloPluginConfiguration config;

    /// Given a psi-element, returns the color and the place where a horizontal line should be
    /// drawn. Returns null if no line should be drawn for the element.
    ///
    /// Note: This method was created with performance in mind. It's engineered as to be fast, and
    /// not make the Dart Editor even slower than it already is.
    ///
    /// Some reference code examples that implement LineMarkerProvider:
    /// https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartServerImplementationsMarkerProvider.java
    /// https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartMethodLineMarkerProvider.java
    /// https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartServerOverrideMarkerProvider.java
    /// https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartServerImplementationsMarkerProvider.java
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {

        // ---

        // 1) Classes, Enums and Extensions.

        if (element instanceof DartClassDefinition ||
                element instanceof DartEnumDefinition ||
                element instanceof DartExtensionDeclaration)
            return lineMarkerInfo_ForClassOrEnumOrExtension(element);

        // ---

        // 2) Test calls and Test-Group calls inside test files.

        if (element instanceof DartCallExpression dce)
            return lineMarkerInfo_ForTestOrGroupCalls(dce);

        // ---

        if (element instanceof PsiComment pc && pc.getTokenType() == SINGLE_LINE_COMMENT) {
            return lineMarkerInfo_ForBddComments(pc);
        }

        // ---

        // 3) Bdd calls and Bdd-Keywords inside test files.

        if ((element instanceof LeafPsiElement) && (element.getParent() instanceof DartId di)
                && (di.getParent() instanceof DartReferenceExpression dre)) {

            // Get the configuration containing the information set by the user in the Settings page.
            Project project = dre.getProject();
            config = MarceloPluginConfiguration.getInstance(project);

            // If the separator setting is turned off, we're done.
            if (!config.ifShowsSeparator_ForBdds) return null;

            var ifIsTestFile = getIfIsTestFile(dre);
            if (!ifIsTestFile) return null;

            // 3.1) Bdd calls.
            // The PSI structure of the Bdd call is, from child to parent:
            // LeafPsiElement -> DartId -> DartReferenceExpression -> DartReferenceExpression.
            // Then the previous sibling of the first DartReferenceExpression is the dot (LeafPsiElement)
            //
            if (dre.getParent() instanceof DartCallExpression dce) {
                {
                    LineMarkerInfo<PsiElement> info = lineMarkerInfo_ForBddCall(di, dce);
                    if (info != null) return info;
                }
            }

            // 3.2) Bdd-Keywords.
            // The PSI structure of the Bdd-Keyword is, from child to parent:
            // LeafPsiElement -> DartId -> DartReferenceExpression -> DartCallExpression ->
            // then DartReferenceExpression and DartCallExpression repeat, until it doesn't.
            //
            if (dre.getParent() instanceof DartReferenceExpression) {
                {
                    LineMarkerInfo<PsiElement> info = lineMarkerInfo_ForBddKeywords(di, dre);
                    //noinspection RedundantIfStatement
                    if (info != null) return info;
                }
            }
        }

        // ---

        // Do not include a separator.
        return null;
    }

    @Nullable
    private LineMarkerInfo<PsiElement> lineMarkerInfo_ForBddCall(DartId di, DartCallExpression dce) {
        String identifier = di.getText();
        if (identifier.equals("Bdd") || identifier.equals("bdd")) {

            /// Find the root element.
            PsiElement currentElement = dce;
            PsiElement rootElement;
            do {
                rootElement = currentElement;
                currentElement = currentElement.getParent();
            }
            while (currentElement instanceof DartReferenceExpression || currentElement instanceof DartCallExpression);

            PsiElement markerLocationElement = getTheExactElementWhereTheMarkerWillBeAdded(rootElement);
            PsiElement anchor = PsiTreeUtil.getDeepestFirst(markerLocationElement);

            var info = new LineMarkerInfo<>(anchor, anchor.getTextRange());
            info.separatorPlacement = SeparatorPlacement.TOP; // Line above the code.
            info.separatorColor = getSeparatorColorDependingOnTheme_ForBddCalls();
            return info;
        }

        return null;
    }

    @Nullable
    private LineMarkerInfo<PsiElement> lineMarkerInfo_ForBddKeywords(DartId di, DartReferenceExpression dre) {

        String identifier = di.getText();

        if (identifier.equals("given") || identifier.equals("then") || identifier.equals("when")
                || identifier.equals("example") || identifier.equals("run")) {

            // Make sure the previous element is a dot.
            var previousElement = dre.getPrevSibling();

            if (previousElement instanceof LeafPsiElement lpe) {
                String text = lpe.getText();
                if (text.equals(".")) {

                    boolean ifWeAreInsideABdd = ifWeAreInsideABddCall(dre);

                    if (ifWeAreInsideABdd) {

                        PsiElement anchor;

                        if (identifier.equals("run")) {
                            // In this case, the dot itself will be the anchor.
                            anchor = lpe;
                        }
                        //
                        else {
                            // Move the separator to above the comments.
                            PsiElement markerLocationElement = getTheExactElementWhereTheMarkerWillBeAdded(lpe);
                            anchor = PsiTreeUtil.getDeepestFirst(markerLocationElement);
                        }

                        var info = new LineMarkerInfo<>(anchor, anchor.getTextRange());
                        info.separatorPlacement = SeparatorPlacement.TOP; // Line above the code.
                        info.separatorColor = getSeparatorColorDependingOnTheme_ForBddKeywords();
                        return info;
                    }
                }
            }
        }

        return null;
    }

    private static boolean ifWeAreInsideABddCall(DartReferenceExpression dre) {

        /// Find the root element.
        PsiElement currentElement = dre;
        PsiElement rootElement;
        do {
            rootElement = currentElement;
            currentElement = currentElement.getParent();
        }
        while (currentElement instanceof DartReferenceExpression || currentElement instanceof DartCallExpression);

        // Now go all the way in, since the Bdd id is the innermost element.
        PsiElement innermost = PsiTreeUtil.getDeepestFirst(rootElement);

        // It must be a leaf element.
        if (!(innermost instanceof LeafPsiElement)) return false;

        // It must be the Bdd identifier.
        String innermostIdentifier = innermost.getText();
        return innermostIdentifier.equals("Bdd") || innermostIdentifier.equals("bdd");
    }

    LineMarkerInfo<PsiElement> lineMarkerInfo_ForClassOrEnumOrExtension(@NotNull PsiElement element) {

        // Get the configuration containing the information set by the user in the Settings page.
        Project project = element.getProject();
        config = MarceloPluginConfiguration.getInstance(project);

        // If the separator setting is turned off, we're done.
        if (!config.ifShowsSeparator_ForClasses) return null;

        // The element above which the line should be drawn. This may not be the original element
        // because if the original element has comments, the line should be above the comments.
        PsiElement markerLocationElement = getTheExactElementWhereTheMarkerWillBeAdded(element);

        var lineMarkerInfo = createLineMarkerInfo_ForClassOrEnumOrExtension(markerLocationElement);

        return lineMarkerInfo;
    }

    LineMarkerInfo<PsiElement> lineMarkerInfo_ForTestOrGroupCalls(
            @NotNull DartCallExpression callExpression) {

        // Get the configuration containing the information set by the user in the Settings page.
        Project project = callExpression.getProject();
        config = MarceloPluginConfiguration.getInstance(project);

        // If the separator setting is turned off, we're done.
        if (!config.ifShowsSeparator_ForTests) return null;

        // If it's NOT a test file, we're done.
        var ifIsTestFile = getIfIsTestFile(callExpression);
        if (!ifIsTestFile) return null;

        // If it's NOT a test call, or a group-test call, we're done.
        @Nullable
        CallExpressionType type = findTestOrTestGroupCallExpression(callExpression);
        if (type == null) return null;

        // Deals with a special case:
        // When a test() is directly inside a group(), the comments will not be right above
        // the callExpression, but instead will the right above the DartStatements that
        // wraps the callExpression. If that's the case, the element we need is the DartStatements.
        PsiElement element;
        PsiElement parent = callExpression.getParent();
        if (parent instanceof DartStatements ds && (ds.getFirstChild() == callExpression))
            element = callExpression.getParent();
        else element = callExpression;

        // The element above which the line should be drawn. This may not be the original element
        // because if the original element has comments, the line should be above the comments.
        PsiElement markerLocationElement = getTheExactElementWhereTheMarkerWillBeAdded(element);

        PsiElement anchor = PsiTreeUtil.getDeepestFirst(markerLocationElement);
        var info = new LineMarkerInfo<>(anchor, anchor.getTextRange());
        info.separatorPlacement = SeparatorPlacement.TOP; // Line above the code.
        info.separatorColor = getSeparatorColorForTestOrTestGroup(type);

        return info;
    }

    LineMarkerInfo<PsiElement> lineMarkerInfo_ForBddComments(@NotNull PsiComment psiComment) {

        // Get the configuration containing the information set by the user in the Settings page.
        Project project = psiComment.getProject();
        config = MarceloPluginConfiguration.getInstance(project);

        // If the separator setting is turned off, we're done.
        if (!config.ifShowsSeparator_ForBdds) return null;

        // If it's NOT a test file, we're done.
        var ifIsTestFile = getIfIsTestFile(psiComment);
        if (!ifIsTestFile) return null;

        String text = psiComment.getText();
        if (text.startsWith("// Given:") || text.startsWith("// When:") || text.startsWith("// Then:") ||
                text.startsWith("// Given/When:") || text.startsWith("// When/Then:")) {

            var info = new LineMarkerInfo<PsiElement>(psiComment, psiComment.getTextRange());
            info.separatorPlacement = SeparatorPlacement.TOP; // Line above the code.
            info.separatorColor = getSeparatorColorDependingOnTheme_ForBddComments();
            return info;
        }
        //
        else return null;
    }

    @NotNull
    private Color getSeparatorColorForTestOrTestGroup(@NotNull CallExpressionType type) {
        Color separatorColor;
        if (type == CallExpressionType.TEST)
            separatorColor = getSeparatorColorDependingOnTheme_ForTestCalls();
            //
        else if (type == CallExpressionType.TEST_GROUP)
            separatorColor = getSeparatorColorDependingOnTheme_ForGroupCalls();
            //
        else throw new AssertionError(type);
        return separatorColor;
    }

    /// Return the current Dart file. If it's not a Dart file (other file type), returns null.
    @Nullable
    private static DartFile getDartFile(@NotNull PsiElement element) {

        PsiFile _file = element.getContainingFile();

        if (_file instanceof DartFile)
            return (DartFile) _file;
        else
            return null;
    }

    /// The element above which the line should be drawn. This may not be the original element
    /// because if the original element has comments, the line should be above the comments.
    @NotNull
    private PsiElement getTheExactElementWhereTheMarkerWillBeAdded(@NotNull PsiElement element) {

        // Start with the given element.
        PsiElement markerLocation = element;

        // Keep moving the marker to the previous sibling, until a non-comment element is found.
        while (true) {
            // Get the previous non-whitespace sibling
            PsiElement prevSibling = getPrevSiblingIgnoringWhiteSpace(markerLocation);

            // If there's no more siblings, we're done.
            if (prevSibling == null) break;

            // If the previous sibling is a comment, move the marker there and continue the loop.
            if (prevSibling instanceof PsiComment) {
                markerLocation = prevSibling;
                continue;
            }

            // If the previous sibling is a whitespace, and the sibling before it is a comment,
            // move the marker to the whitespace and continue the loop.
            if (prevSibling instanceof PsiWhiteSpace) {
                PsiElement prevPrevSibling = getPrevSiblingIgnoringWhiteSpace(prevSibling);
                if (prevPrevSibling instanceof PsiComment) {
                    markerLocation = prevSibling;
                    continue;
                }
            }

            // If we've encountered a non-comment, non-whitespace sibling, we're done.
            break;
        }

        // Get the non-whitespace sibling before the marker.
        PsiElement prevElement = getPrevSiblingIgnoringWhiteSpace(markerLocation);

        // If there's no such sibling, return the original element
        // to indicate that the marker shouldn't be moved.
        if (prevElement == null) return element;

        return markerLocation;
    }

    /// Helper function that returns the previous sibling of the given element,
    /// skipping all PsiWhiteSpace instances.
    private PsiElement getPrevSiblingIgnoringWhiteSpace(PsiElement element) {
        var prevSibling = element.getPrevSibling();
        while (prevSibling instanceof PsiWhiteSpace) {
            prevSibling = prevSibling.getPrevSibling();
        }
        return prevSibling;
    }

    @NotNull
    private LineMarkerInfo<PsiElement> createLineMarkerInfo_ForClassOrEnumOrExtension(
            PsiElement markerLocationElement) {

        PsiElement anchor = PsiTreeUtil.getDeepestFirst(markerLocationElement);
        var info = new LineMarkerInfo<>(anchor, anchor.getTextRange());
        info.separatorPlacement = SeparatorPlacement.TOP; // Line above the code.
        info.separatorColor = getSeparatorColorDependingOnTheme_ForClass();

        return info;
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForClass() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForClasses_DarkTheme
                        : config.separatorColor_ForClasses_LightTheme);
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForTestCalls() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForTestCalls_DarkTheme
                        : config.separatorColor_ForTestCalls_LightTheme);
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForGroupCalls() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForGroupCalls_DarkTheme
                        : config.separatorColor_ForGroupCalls_LightTheme);
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForBddCalls() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForBddCalls_DarkTheme
                        : config.separatorColor_ForBddCalls_LightTheme);
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForBddKeywords() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForBddKeywords_DarkTheme
                        : config.separatorColor_ForBddKeywords_LightTheme);
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForBddComments() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForBddComments_DarkTheme
                        : config.separatorColor_ForBddComments_LightTheme);
    }

    /// Note 1: We check the background color directly, since checking if the theme is Darcula
    /// doesn't work when the user modifies the color directly.
    private static double brightness() {
        EditorColorsScheme scheme = EditorColorsManager.getInstance().getGlobalScheme();
        Color bkgColor = scheme.getDefaultBackground();

        float[] hsbVals = Color.RGBtoHSB(bkgColor.getRed(), bkgColor.getGreen(), bkgColor.getBlue(), null);
        double brightness = hsbVals[2];
        return brightness;
    }

    /// Return non-null only if this is a `test()` or `group()` call.
    @Nullable
    private CallExpressionType findTestOrTestGroupCallExpression(
            @NotNull final DartCallExpression dartCallExpression) {

        final DartExpression dartExpression = dartCallExpression.getExpression();
        if (dartExpression == null) return null;
        final DartReference dartReference = (DartReference) dartExpression.getReference();
        if (dartReference == null) return null;

        PsiElement resolve = dartReference.resolve();

        String name = null;
        if (resolve instanceof DartComponentName dcn) name = dcn.getName();
        if (name == null) name = "";

        return switch (name) {

            case "test", "testWidgets", "setUp", "setUpAll",
                    "tearDown", "tearDownAll" -> CallExpressionType.TEST;

            case "group" -> CallExpressionType.TEST_GROUP;

            default -> null;
        };
    }

    /// Returns true if the file is a test file.
    private static boolean getIfIsTestFile(@NotNull final PsiElement element) {
        DartFile file = getDartFile(element);
        if (file == null) return false;
        String fileName = file.getName();
        return fileName.endsWith("_test.dart")
                || fileName.endsWith("_test_driver.dart")
                || fileName.startsWith("bdd_")
                || fileName.startsWith("BDD_");
    }
}
