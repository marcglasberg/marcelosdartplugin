package dev.glasberg.marcelosdartplugin;

import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.colors.EditorColorsManager;
import com.intellij.openapi.editor.colors.EditorColorsScheme;
import com.intellij.openapi.editor.markup.SeparatorPlacement;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiComment;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiWhiteSpace;
import com.intellij.psi.util.PsiTreeUtil;
import com.jetbrains.lang.dart.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.*;

public class MarceloLineMarkerProvider implements LineMarkerProvider {

    // import com.intellij.openapi.diagnostic.Logger;
    // private static final Logger LOG = Logger.getInstance(MarceloLineMarkerProvider.class);

    enum CallExpression {
        TEST,
        TEST_GROUP,
    }

    private MarceloPluginConfiguration config;

    /// Given a psi-element, returns the color and the place where a horizontal line should be
    // drawn. Returns null is no line should be drawn for the element.
    ///
    /// Note: This method is performance minded. It's engineered as to be fast, and not make the
    /// Dart Editor even slower that it already is.
    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(final @NotNull PsiElement element) {

        // Some examples:
        // https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartServerImplementationsMarkerProvider.java
        // https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartMethodLineMarkerProvider.java
        // https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartServerOverrideMarkerProvider.java
        // https://github.com/JetBrains/intellij-plugins/blob/master/Dart/src/com/jetbrains/lang/dart/ide/marker/DartServerImplementationsMarkerProvider.java

        // ---

        // First we want to quickly eliminate most elements, for performance reasons.

        boolean ifIsClassOrSimilar = element instanceof DartClassDefinition ||
                element instanceof DartEnumDefinition ||
                element instanceof DartExtensionDeclaration;

        boolean ifIsCallExpression = element instanceof DartCallExpression;

        // If the element is not a markable element, we're done.
        if (!ifIsClassOrSimilar && !ifIsCallExpression) return null;

        // ---

        // Get the configuration containing the information set by the user in the Settings page.
        Project project = element.getProject();
        config = MarceloPluginConfiguration.getInstance(project);

        // If all separator settings are turned off, we're done.
        if (ifIsClassOrSimilar && !config.ifShowsSeparators_ForClasses) return null;

        // If all separator settings are turned off, we're done.
        if (ifIsCallExpression && !config.ifShowsSeparators_ForTestOrGroupCalls) return null;

        // ---

        CallExpression callExpression;
        if (ifIsClassOrSimilar) {
            // Class, enum and extension declarations: just continue. We don't need any more checks.
            callExpression = null;
        }
        //
        // When ifIsCallExpression is true, element is a DartCallExpression.
        else {
            callExpression = findTestOrTestGroupCallExpression((DartCallExpression) element);

            // If it's NOT a test or group-test call, inside a test file,
            // and with the correct setting on, we're done.
            if (callExpression == null || !config.ifShowsSeparators_ForTestOrGroupCalls)
                return null;
        }

        // ---

        // The element above which the line should be drawn. This may not be the original element
        // because if the original element has comments, the line should be above the comments.
        PsiElement markerLocationElement = getTheExactElementWhereTheMarkerWillBeAdded(element);
        if (markerLocationElement == null) return null;

        var lineMarkerInfo = createLineMarkerInfo(markerLocationElement, ifIsClassOrSimilar, callExpression);

        return lineMarkerInfo;
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

    @Nullable
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

        // If there's no such sibling, return null to indicate that the marker shouldn't be moved.
        if (prevElement == null) return null;

        return markerLocation;
    }

    /// Helper function that returns the previous sibling of the given element,
    // skipping all PsiWhiteSpace instances.
    private PsiElement getPrevSiblingIgnoringWhiteSpace(PsiElement element) {
        var prevSibling = element.getPrevSibling();
        while (prevSibling instanceof PsiWhiteSpace) {
            prevSibling = prevSibling.getPrevSibling();
        }
        return prevSibling;
    }

    @NotNull
    private LineMarkerInfo<PsiElement> createLineMarkerInfo(PsiElement markerLocationElement,
                                                            boolean ifIsClassOrEnum,
                                                            CallExpression callExpression) {

        PsiElement anchor = PsiTreeUtil.getDeepestFirst(markerLocationElement);

        var info = new LineMarkerInfo<>(anchor, anchor.getTextRange());

        // The horizontal line should be ABOVE the line.
        info.separatorPlacement = SeparatorPlacement.TOP;

        if (ifIsClassOrEnum)
            info.separatorColor = getSeparatorColorDependingOnTheme_ForClass();
        else  //
            if (callExpression == CallExpression.TEST)
                info.separatorColor = getSeparatorColorDependingOnTheme_ForTestOrGroupCalls();
            else if (callExpression == CallExpression.TEST_GROUP)
                info.separatorColor = getSeparatorColorDependingOnTheme_ForTestGroupCalls();
            else throw new AssertionError(callExpression);

        return info;
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForClass() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForClasses_DarkThemeX
                        : config.separatorColor_ForClasses_LightThemeX);
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForTestOrGroupCalls() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForTestOrGroupCalls_DarkTheme
                        : config.separatorColor_ForTestOrGroupCalls_LightTheme);
    }

    @SuppressWarnings("UseJBColor")
    private Color getSeparatorColorDependingOnTheme_ForTestGroupCalls() {

        return new Color(
                (brightness() < 0.5)
                        ? config.separatorColor_ForTestGroupCalls_DarkThemeX
                        : config.separatorColor_ForTestGroupCalls_LightTheme);
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

    /// Return non-null only if this is a `test()` or `group()` call, in a test file.
    private CallExpression findTestOrTestGroupCallExpression(
            @NotNull final DartCallExpression callExpression) {

        var ifIsTestFile = getIfIsTestFile(callExpression);

        if (ifIsTestFile) {

            // Identify test function calls.
            final DartExpression dartExpression = callExpression.getExpression();
            if (dartExpression == null) return null;
            final DartReference dartReference = (DartReference) dartExpression.getReference();

            if (dartReference != null) {
                final String text = dartReference.getCanonicalText();
                if (text.equals("test")) return CallExpression.TEST;
                if (text.equals("group")) return CallExpression.TEST_GROUP;
            } else return null;
        }
        //
        return null;
    }

    /// Returns true if the file is a test file.
    private static boolean getIfIsTestFile(@NotNull final PsiElement element) {
        DartFile file = getDartFile(element);
        if (file == null) return false;
        return file.getName().endsWith("_test.dart");
    }

}
