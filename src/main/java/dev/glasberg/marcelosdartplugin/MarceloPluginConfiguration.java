package dev.glasberg.marcelosdartplugin;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.Service;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.ui.Gray;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.intellij.util.xmlb.annotations.Property;
import org.jetbrains.annotations.NotNull;

@Service(Service.Level.PROJECT)
@State(name = "MyPluginConfiguration", storages = {@Storage("marcelosdartplugin.xml")})
public final class MarceloPluginConfiguration implements PersistentStateComponent<MarceloPluginConfiguration> {

    // --------------

    // For Classes:

    public static boolean defaultIfShowsSeparator_ForClasses = true;
    public static int defaultSeparatorColor_ForClasses_LightTheme = Gray._50.getRGB();
    public static int defaultSeparatorColor_ForClasses_DarkTheme = Gray._200.getRGB();


    // For Tests:

    public static boolean defaultIfShowsSeparator_ForTests = true;
    public static int defaultSeparatorColor_ForTestCalls_LightTheme = Gray._200.getRGB();
    public static int defaultSeparatorColor_ForTestCalls_DarkTheme = Gray._90.getRGB();
    public static int defaultSeparatorColor_ForGroupCalls_LightTheme = Gray._50.getRGB();
    public static int defaultSeparatorColor_ForGroupCalls_DarkTheme = Gray._200.getRGB();

    // For Bdds:

    public static boolean defaultIfShowsSeparator_ForBdds = true;
    public static int defaultSeparatorColor_ForBddCalls_LightTheme = Gray._50.getRGB();
    public static int defaultSeparatorColor_ForBddCalls_DarkTheme = Gray._200.getRGB();
    public static int defaultSeparatorColor_ForBddKeywords_LightTheme = Gray._200.getRGB();
    public static int defaultSeparatorColor_ForBddKeywords_DarkTheme = Gray._90.getRGB();
    public static int defaultSeparatorColor_ForBddComments_LightTheme = Gray._220.getRGB();
    public static int defaultSeparatorColor_ForBddComments_DarkTheme = Gray._70.getRGB();

    // --------------

    @Property
    public boolean ifShowsSeparator_ForClasses = defaultIfShowsSeparator_ForClasses;

    @Property
    public int separatorColor_ForClasses_LightTheme = defaultSeparatorColor_ForClasses_LightTheme;

    @Property
    public int separatorColor_ForClasses_DarkTheme = defaultSeparatorColor_ForClasses_DarkTheme;

    // --------------

    @Property
    public boolean ifShowsSeparator_ForTests = defaultIfShowsSeparator_ForTests;

    @Property
    public int separatorColor_ForTestCalls_LightTheme = defaultSeparatorColor_ForTestCalls_LightTheme;

    @Property
    public int separatorColor_ForTestCalls_DarkTheme = defaultSeparatorColor_ForTestCalls_DarkTheme;

    @Property
    public int separatorColor_ForGroupCalls_LightTheme = defaultSeparatorColor_ForGroupCalls_LightTheme;

    @Property
    public int separatorColor_ForGroupCalls_DarkTheme = defaultSeparatorColor_ForGroupCalls_DarkTheme;

    // --------------

    @Property
    public boolean ifShowsSeparator_ForBdds = defaultIfShowsSeparator_ForBdds;

    @Property
    public int separatorColor_ForBddCalls_LightTheme = defaultSeparatorColor_ForBddCalls_LightTheme;

    @Property
    public int separatorColor_ForBddCalls_DarkTheme = defaultSeparatorColor_ForBddCalls_DarkTheme;

    @Property
    public int separatorColor_ForBddKeywords_LightTheme = defaultSeparatorColor_ForBddKeywords_LightTheme;

    @Property
    public int separatorColor_ForBddKeywords_DarkTheme = defaultSeparatorColor_ForBddKeywords_DarkTheme;

    @Property
    public int separatorColor_ForBddComments_LightTheme = defaultSeparatorColor_ForBddComments_LightTheme;

    @Property
    public int separatorColor_ForBddComments_DarkTheme = defaultSeparatorColor_ForBddComments_DarkTheme;

    // --------------

    public static MarceloPluginConfiguration getInstance(Project project) {
        return project.getService(MarceloPluginConfiguration.class);
    }

    @NotNull
    @Override
    public MarceloPluginConfiguration getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull MarceloPluginConfiguration state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}





