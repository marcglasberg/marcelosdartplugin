package dev.glasberg.marcelosdartplugin;

import com.intellij.openapi.options.Configurable;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.NlsContexts;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

public class MarceloPluginConfigurable implements Configurable {
    private MarceloPluginConfigurationForm form;
    private final MarceloPluginConfiguration config;

    public MarceloPluginConfigurable(Project project) {
        config = MarceloPluginConfiguration.getInstance(project);
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
        form = new MarceloPluginConfigurationForm();
        return form.getMainPanel();
    }

    @Override
    public boolean isModified() {

        boolean classSettingModified = form.get_IfShowsSeparator_ForClasses_CheckBox() != config.ifShowsSeparators_ForClasses;
        boolean classLightThemeColorModified = !form.get_SeparatorColor_ForClasses_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForClasses_LightThemeX));
        boolean classDarkThemeColorModified = !form.get_SeparatorColor_ForClasses_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForClasses_DarkThemeX));

        boolean testsSettingModified = form.get_ShowSeparator_ForTests_CheckBox() != config.ifShowsSeparators_ForTestOrGroupCalls;
        boolean testsLightThemeColorModified = !form.get_SeparatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForTestOrGroupCalls_LightTheme));
        boolean testsDarkThemeColorModified = !form.get_SeparatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForTestOrGroupCalls_DarkTheme));
        boolean testGroupLightThemeColorModified = !form.get_SeparatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel().equals(deserialize(config.separatorColor_ForTestOrGroupCalls_LightTheme));
        boolean testGroupDarkThemeColorModified = !form.get_SeparatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel().equals(deserialize(config.separatorColor_ForTestOrGroupCalls_DarkTheme));

        return classSettingModified || classLightThemeColorModified || classDarkThemeColorModified
                || testsSettingModified || testsLightThemeColorModified || testsDarkThemeColorModified
                || testGroupLightThemeColorModified || testGroupDarkThemeColorModified;
    }

    @Override
    public void apply() {
        config.ifShowsSeparators_ForClasses = form.get_IfShowsSeparator_ForClasses_CheckBox();
        config.separatorColor_ForClasses_LightThemeX = serialize(form.get_SeparatorColor_ForClasses_LightTheme_ColorPanel());
        config.separatorColor_ForClasses_DarkThemeX = serialize(form.get_SeparatorColor_ForClasses_DarkTheme_ColorPanel());

        config.ifShowsSeparators_ForTestOrGroupCalls = form.get_ShowSeparator_ForTests_CheckBox();
        config.separatorColor_ForTestOrGroupCalls_LightTheme = serialize(form.get_SeparatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel());
        config.separatorColor_ForTestOrGroupCalls_DarkTheme = serialize(form.get_SeparatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel());

        config.separatorColor_ForTestGroupCalls_LightTheme = serialize(form.get_SeparatorColor_ForTestGroupCalls_LightTheme_ColorPanel());
        config.separatorColor_ForTestGroupCalls_DarkThemeX = serialize(form.get_SeparatorColor_ForTestGroupCalls_DarkTheme_ColorPanel());
    }

    @Override
    public void reset() {
        form.set_IfShowsSeparator_ForClasses_CheckBox(config.ifShowsSeparators_ForClasses);
        form.set_SeparatorColor_ForClasses_LightTheme_ColorPanel(deserialize(config.separatorColor_ForClasses_LightThemeX));
        form.set_SeparatorColor_ForClasses_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForClasses_DarkThemeX));

        form.set_ShowSeparator_ForTestOrGroupCalls_CheckBox(config.ifShowsSeparators_ForTestOrGroupCalls);
        form.set_SeparatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel(deserialize(config.separatorColor_ForTestOrGroupCalls_LightTheme));
        form.set_SeparatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForTestOrGroupCalls_DarkTheme));

        form.set_SeparatorColor_ForTestGroupCalls_LightTheme_ColorPanel(deserialize(config.separatorColor_ForTestGroupCalls_LightTheme));
        form.set_SeparatorColor_ForTestGroupCalls_DarkTheme_ColorPanel(deserialize(config.separatorColor_ForTestGroupCalls_DarkThemeX));
    }

    private static int serialize(Color color) {
        return color.getRGB();
    }

    @SuppressWarnings("UseJBColor")
    private static Color deserialize(int serializedColor) {
        return new Color(serializedColor);
    }
}
