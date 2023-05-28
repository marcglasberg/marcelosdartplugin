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

import java.awt.*;

@Service(Service.Level.PROJECT)
@State(name = "MyPluginConfiguration", storages = {@Storage("marcelosdartplugin.xml")})
public final class MarceloPluginConfiguration implements PersistentStateComponent<MarceloPluginConfiguration> {

    // --------------

    @Property
    public boolean ifShowsSeparators_ForClasses = true;

    @Property
    public int separatorColor_ForClasses_LightTheme = Color.BLACK.getRGB();

    @Property
    public int separatorColor_ForClasses_DarkTheme = Color.WHITE.getRGB();

    // --------------

    @Property
    public boolean ifShowsSeparators_ForTestOrGroupCalls = true;

    @Property
    public int separatorColor_ForTestOrGroupCalls_LightTheme = Gray._200.getRGB();

    @Property
    public int separatorColor_ForTestOrGroupCalls_DarkTheme = Gray._90.getRGB();

    @Property
    public int separatorColor_ForTestGroupCalls_LightTheme = Color.BLACK.getRGB();

    @Property
    public int separatorColor_ForTestGroupCalls_DarkTheme = Color.WHITE.getRGB();

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





