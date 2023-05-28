package dev.glasberg.marcelosdartplugin;

import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.ColorPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;

public class MarceloPluginConfigurationForm {
    private final JPanel mainPanel;

    // ---

    private JCheckBox ifShowsSeparator_ForClasses_CheckBox;
    private ColorPanel separatorColor_ForClasses_LightTheme_ColorPanel, separatorColor_ForClasses_DarkTheme_ColorPanel;

    // ---

    private JCheckBox showSeparator_ForTestOrGroupCalls_CheckBox;
    private ColorPanel separatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel, separatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel;

    private ColorPanel separatorColor_ForTestGroupCalls_LightTheme_ColorPanel, separatorColor_ForTestGroupCalls_DarkTheme_ColorPanel;

    public MarceloPluginConfigurationForm() {

        mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridy = -1;

        classesSeparator_Section(constraints);
        testAndGroupCallsSeparator_Section(constraints);

        // ---

        // The last box occupies all the rest of the vertical space.
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.gridy++;
        JPanel lastBox = new JPanel();
        mainPanel.add(lastBox, constraints);
    }

    private void classesSeparator_Section(GridBagConstraints constraints) {

        ifShowsSeparator_ForClasses_CheckBox = new JCheckBox("Show");
        separatorColor_ForClasses_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForClasses_DarkTheme_ColorPanel = new ColorPanel();

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.emptyInsets(); // 5px below each component.
        mainPanel.add(sectionLabel("Class separator", true), constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 15, 40);

        @NlsContexts.Label
        JLabel explanation = new JLabel(
                "Draws horizontal line separators above class and enum definitions.");
        explanation.setForeground(UIManager.getColor("Label.disabledForeground"));
        mainPanel.add(explanation, constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 10, 0);
        mainPanel.add(ifShowsSeparator_ForClasses_CheckBox, constraints);

        // ---

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Separator color for <b>classes</b>, <b>light</b> themes:</html>"));
        panel.add(separatorColor_ForClasses_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Separator color for <b>classes</b>, <b>dark</b> themes:</html>"));
        panel.add(separatorColor_ForClasses_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);
    }

    private void testAndGroupCallsSeparator_Section(GridBagConstraints constraints) {

        showSeparator_ForTestOrGroupCalls_CheckBox = new JCheckBox("Show");
        separatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel = new ColorPanel();
        separatorColor_ForTestGroupCalls_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForTestGroupCalls_DarkTheme_ColorPanel = new ColorPanel();

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.emptyInsets(); // 5px below each component.
        mainPanel.add(sectionLabel("Test separator"), constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 15, 40);
        JLabel explanation = new JLabel(
                "Draws horizontal line separators above each test and test-group in a test file.");
        explanation.setForeground(UIManager.getColor("Label.disabledForeground"));
        mainPanel.add(explanation, constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 10, 0);
        mainPanel.add(showSeparator_ForTestOrGroupCalls_CheckBox, constraints);

        // ---

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Separator color for <b>test()</b> calls, <b>light</b> themes:</html>"));
        panel.add(separatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Separator color for <b>test()</b> calls, <b>dark</b> themes:</html>"));
        panel.add(separatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Separator color for <b>group()</b> calls, <b>light</b> themes:</html>"));
        panel.add(separatorColor_ForTestGroupCalls_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insets(20, 20, 0, 0);
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Separator color for <b>group()</b> calls, <b>dark</b> themes:</html>"));
        panel.add(separatorColor_ForTestGroupCalls_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);
    }

    JComponent sectionLabel(String labelText) {
        return sectionLabel(labelText, false);
    }

    JComponent sectionLabel(String labelText, boolean isFirst) {
        JPanel row = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();

        // Add the label
        constraints.fill = GridBagConstraints.NONE;  // don't stretch
        constraints.weightx = 0;  // don't take any extra horizontal space
        constraints.gridy = 0; // place on the first row
        constraints.gridx = 0; // place on the first column
        row.add(new JLabel(labelText), constraints);

        // Add the separator
        constraints.fill = GridBagConstraints.HORIZONTAL;  // stretch horizontally
        constraints.weightx = 1;  // take all extra horizontal space
        constraints.gridy = 0; // place on the first row
        constraints.gridx = 1; // place on the second column
        constraints.insets = JBUI.insets(3, 5, 0, 0);
        row.add(new JSeparator(), constraints);

        row.setBorder(BorderFactory.createEmptyBorder(isFirst ? 10 : 30, 0, 15, 0));

        return row;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public boolean get_IfShowsSeparator_ForClasses_CheckBox() {
        return ifShowsSeparator_ForClasses_CheckBox.isSelected();
    }

    public void set_IfShowsSeparator_ForClasses_CheckBox(boolean showSeparators) {
        this.ifShowsSeparator_ForClasses_CheckBox.setSelected(showSeparators);
    }

    public Color get_SeparatorColor_ForClasses_LightTheme_ColorPanel() {
        return separatorColor_ForClasses_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForClasses_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForClasses_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForClasses_DarkTheme_ColorPanel() {
        return separatorColor_ForClasses_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForClasses_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForClasses_DarkTheme_ColorPanel.setSelectedColor(color);
    }

    public boolean get_ShowSeparator_ForTests_CheckBox() {
        return showSeparator_ForTestOrGroupCalls_CheckBox.isSelected();
    }

    public void set_ShowSeparator_ForTestOrGroupCalls_CheckBox(boolean showSeparators) {
        this.showSeparator_ForTestOrGroupCalls_CheckBox.setSelected(showSeparators);
    }

    public Color get_SeparatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel() {
        return separatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForTestOrGroupCalls_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel() {
        return separatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForTestOrGroupCalls_DarkTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForTestGroupCalls_LightTheme_ColorPanel() {
        return separatorColor_ForTestGroupCalls_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForTestGroupCalls_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForTestGroupCalls_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForTestGroupCalls_DarkTheme_ColorPanel() {
        return separatorColor_ForTestGroupCalls_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForTestGroupCalls_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForTestGroupCalls_DarkTheme_ColorPanel.setSelectedColor(color);
    }
}

