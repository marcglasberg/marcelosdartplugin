package dev.glasberg.marcelosdartplugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

import static dev.glasberg.marcelosdartplugin.MarceloPluginConfiguration.*;

public class MarceloPluginConfigurable implements Configurable {
    private MarceloPluginConfigurationForm form;
    private final MarceloPluginConfiguration config;

    public MarceloPluginConfigurable(Project project) {
        config = MarceloPluginConfiguration.getInstance(project);
    }

    public void resetToDefaults() {

        // For Classes:
        config.ifShowsSeparator_ForClasses = defaultIfShowsSeparator_ForClasses;
        config.separatorColor_ForClasses_LightTheme = defaultSeparatorColor_ForClasses_LightTheme;
        config.separatorColor_ForClasses_DarkTheme = defaultSeparatorColor_ForClasses_DarkTheme;

        // For Tests:
        config.ifShowsSeparator_ForTests = defaultIfShowsSeparator_ForTests;
        config.separatorColor_ForTestCalls_LightTheme = defaultSeparatorColor_ForTestCalls_LightTheme;
        config.separatorColor_ForTestCalls_DarkTheme = defaultSeparatorColor_ForTestCalls_DarkTheme;
        config.separatorColor_ForGroupCalls_LightTheme = defaultSeparatorColor_ForGroupCalls_LightTheme;
        config.separatorColor_ForGroupCalls_DarkTheme = defaultSeparatorColor_ForGroupCalls_DarkTheme;

        // For Bdds:
        config.ifShowsSeparator_ForBdds = defaultIfShowsSeparator_ForBdds;
        config.separatorColor_ForBddCalls_LightTheme = defaultSeparatorColor_ForBddCalls_LightTheme;
        config.separatorColor_ForBddCalls_DarkTheme = defaultSeparatorColor_ForBddCalls_DarkTheme;
        config.separatorColor_ForBddKeywords_LightTheme = defaultSeparatorColor_ForBddKeywords_LightTheme;
        config.separatorColor_ForBddKeywords_DarkTheme = defaultSeparatorColor_ForBddKeywords_DarkTheme;
        config.separatorColor_ForBddComments_LightTheme = defaultSeparatorColor_ForBddComments_LightTheme;
        config.separatorColor_ForBddComments_DarkTheme = defaultSeparatorColor_ForBddComments_DarkTheme;

        reset();
    }

    @Nls(capitalization = Nls.Capitalization.Title)
    @NlsContexts.ConfigurableName
    @Override
    public String getDisplayName() {
        return "Marcelo's Flutter/Dart Essentials";
    }

    @Nullable
    @Override
    public JComponent createComponent() {
        form = new MarceloPluginConfigurationForm(this);
        return form.getMainPanel();
    }

    @Override
    public boolean isModified() {

        boolean ifModified_ShowsSeparators_ForClasses = form.get_IfShowsSeparator_ForClasses_CheckBox() != config.ifShowsSeparator_ForClasses;
        boolean ifModified_SeparatorColor_ForClasses_LightTheme = !form.get_SeparatorColor_ForClasses_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForClasses_LightTheme));
        boolean isModified_SeparatorColor_ForClasses_DarkTheme = !form.get_SeparatorColor_ForClasses_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForClasses_DarkTheme));

        boolean ifModified_ShowsSeparators_ForTests = form.get_IfShowsSeparator_ForTests_CheckBox() != config.ifShowsSeparator_ForTests;
        boolean ifModified_SeparatorColor_ForTestCalls_LightTheme = !form.get_SeparatorColor_ForTestCalls_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForTestCalls_LightTheme));
        boolean ifModified_SeparatorColor_ForTestCalls_DarkTheme = !form.get_SeparatorColor_ForTestCalls_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForTestCalls_DarkTheme));
        boolean ifModified_SeparatorColor_ForGroupCalls_LightTheme = !form.get_SeparatorColor_ForGroupCalls_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForGroupCalls_LightTheme));
        boolean ifModified_SeparatorColor_ForGroupCalls_DarkTheme = !form.get_SeparatorColor_ForGroupCalls_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForGroupCalls_DarkTheme));

        boolean ifModified_ShowsSeparators_ForBdds = form.get_IfShowsSeparator_ForBdds_CheckBox() != config.ifShowsSeparator_ForBdds;
        boolean ifModified_SeparatorColor_ForBddCalls_LightTheme = !form.get_SeparatorColor_ForBddCalls_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForBddCalls_LightTheme));
        boolean ifModified_SeparatorColor_ForBddCalls_DarkTheme = !form.get_SeparatorColor_ForBddCalls_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForBddCalls_DarkTheme));
        boolean ifModified_SeparatorColor_ForBddKeywords_LightTheme = !form.get_SeparatorColor_ForBddKeywords_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForBddKeywords_LightTheme));
        boolean ifModified_SeparatorColor_ForBddKeywords_DarkTheme = !form.get_SeparatorColor_ForBddKeywords_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForBddKeywords_DarkTheme));
        boolean ifModified_SeparatorColor_ForBddComments_LightTheme = !form.get_SeparatorColor_ForBddComments_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForBddComments_LightTheme));
        boolean ifModified_SeparatorColor_ForBddComments_DarkTheme = !form.get_SeparatorColor_ForBddComments_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForBddComments_DarkTheme));

        return ifModified_ShowsSeparators_ForClasses || ifModified_SeparatorColor_ForClasses_LightTheme
                || isModified_SeparatorColor_ForClasses_DarkTheme || ifModified_ShowsSeparators_ForTests
                || ifModified_SeparatorColor_ForTestCalls_LightTheme || ifModified_SeparatorColor_ForTestCalls_DarkTheme
                || ifModified_SeparatorColor_ForGroupCalls_LightTheme || ifModified_SeparatorColor_ForGroupCalls_DarkTheme
                || ifModified_ShowsSeparators_ForBdds || ifModified_SeparatorColor_ForBddCalls_LightTheme
                || ifModified_SeparatorColor_ForBddCalls_DarkTheme || ifModified_SeparatorColor_ForBddKeywords_LightTheme
                || ifModified_SeparatorColor_ForBddKeywords_DarkTheme || ifModified_SeparatorColor_ForBddComments_LightTheme
                || ifModified_SeparatorColor_ForBddComments_DarkTheme;
    }

    @Override
    public void apply() {
        config.ifShowsSeparator_ForClasses = form.get_IfShowsSeparator_ForClasses_CheckBox();
        config.separatorColor_ForClasses_LightTheme = serialize(form.get_SeparatorColor_ForClasses_LightTheme_ColorPanel());
        config.separatorColor_ForClasses_DarkTheme = serialize(form.get_SeparatorColor_ForClasses_DarkTheme_ColorPanel());

        config.ifShowsSeparator_ForTests = form.get_IfShowsSeparator_ForTests_CheckBox();
        config.separatorColor_ForTestCalls_LightTheme = serialize(form.get_SeparatorColor_ForTestCalls_LightTheme_ColorPanel());
        config.separatorColor_ForTestCalls_DarkTheme = serialize(form.get_SeparatorColor_ForTestCalls_DarkTheme_ColorPanel());
        config.separatorColor_ForGroupCalls_LightTheme = serialize(form.get_SeparatorColor_ForGroupCalls_LightTheme_ColorPanel());
        config.separatorColor_ForGroupCalls_DarkTheme = serialize(form.get_SeparatorColor_ForGroupCalls_DarkTheme_ColorPanel());

        config.ifShowsSeparator_ForBdds = form.get_IfShowsSeparator_ForBdds_CheckBox();
        config.separatorColor_ForBddCalls_LightTheme = serialize(form.get_SeparatorColor_ForBddCalls_LightTheme_ColorPanel());
        config.separatorColor_ForBddCalls_DarkTheme = serialize(form.get_SeparatorColor_ForBddCalls_DarkTheme_ColorPanel());
        config.separatorColor_ForBddKeywords_LightTheme = serialize(form.get_SeparatorColor_ForBddKeywords_LightTheme_ColorPanel());
        config.separatorColor_ForBddKeywords_DarkTheme = serialize(form.get_SeparatorColor_ForBddKeywords_DarkTheme_ColorPanel());
        config.separatorColor_ForBddComments_LightTheme = serialize(form.get_SeparatorColor_ForBddComments_LightTheme_ColorPanel());
        config.separatorColor_ForBddComments_DarkTheme = serialize(form.get_SeparatorColor_ForBddComments_DarkTheme_ColorPanel());
    }

    @Override
    public void reset() {
        form.set_IfShowsSeparator_ForClasses_CheckBox(config.ifShowsSeparator_ForClasses);
        form.set_SeparatorColor_ForClasses_LightTheme_ColorPanel(deserialize(config.separatorColor_ForClasses_LightTheme));
        form.set_SeparatorColor_ForClasses_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForClasses_DarkTheme));

        form.set_IfShowsSeparator_ForTests_CheckBox(config.ifShowsSeparator_ForTests);
        form.set_SeparatorColor_ForTestCalls_LightTheme_ColorPanel(deserialize(config.separatorColor_ForTestCalls_LightTheme));
        form.set_SeparatorColor_ForTestCalls_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForTestCalls_DarkTheme));
        form.set_SeparatorColor_ForGroupCalls_LightTheme_ColorPanel(deserialize(config.separatorColor_ForGroupCalls_LightTheme));
        form.set_SeparatorColor_ForGroupCalls_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForGroupCalls_DarkTheme));

        form.set_IfShowsSeparator_ForBdds_CheckBox(config.ifShowsSeparator_ForBdds);
        form.set_SeparatorColor_ForBddCalls_LightTheme_ColorPanel(deserialize(config.separatorColor_ForBddCalls_LightTheme));
        form.set_SeparatorColor_ForBddCalls_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForBddCalls_DarkTheme));
        form.set_SeparatorColor_ForBddKeywords_LightTheme_ColorPanel(deserialize(config.separatorColor_ForBddKeywords_LightTheme));
        form.set_SeparatorColor_ForBddKeywords_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForBddKeywords_DarkTheme));
        form.set_SeparatorColor_ForBddComments_LightTheme_ColorPanel(deserialize(config.separatorColor_ForBddComments_LightTheme));
        form.set_SeparatorColor_ForBddComments_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForBddComments_DarkTheme));
    }

    private static int serialize(Color color) {
        return color.getRGB();
    }

    @SuppressWarnings("UseJBColor")
    private static Color deserialize(int serializedColor) {
        return new Color(serializedColor);
    }
}
