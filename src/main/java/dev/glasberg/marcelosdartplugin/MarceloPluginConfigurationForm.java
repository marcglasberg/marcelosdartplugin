package dev.glasberg.marcelosdartplugin;

import com.intellij.openapi.util.NlsContexts;
import com.intellij.ui.ColorPanel;
import com.intellij.util.ui.JBUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class MarceloPluginConfigurationForm {
    private final JPanel mainPanel;

    // ---

    private JCheckBox ifShowsSeparator_ForClasses_CheckBox;
    private ColorPanel separatorColor_ForClasses_LightTheme_ColorPanel, separatorColor_ForClasses_DarkTheme_ColorPanel;

    // ---

    private JCheckBox ifShowsSeparator_ForTestOrGroupCalls_CheckBox;
    private ColorPanel separatorColor_ForTestCalls_LightTheme_ColorPanel, separatorColor_ForTestCalls_DarkTheme_ColorPanel;
    private ColorPanel separatorColor_ForGroupCalls_LightTheme_ColorPanel, separatorColor_ForGroupCalls_DarkTheme_ColorPanel;

    // ---

    private JCheckBox ifShowsSeparator_ForBdds_CheckBox;
    private ColorPanel separatorColor_ForBddCalls_LightTheme_ColorPanel, separatorColor_ForBddCalls_DarkTheme_ColorPanel;
    private ColorPanel separatorColor_ForBddKeywords_LightTheme_ColorPanel, separatorColor_ForBddKeywords_DarkTheme_ColorPanel;
    private ColorPanel separatorColor_ForBddComments_LightTheme_ColorPanel, separatorColor_ForBddComments_DarkTheme_ColorPanel;

    public MarceloPluginConfigurationForm(MarceloPluginConfigurable configurable) {

        mainPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx = 1.0;
        constraints.gridy = -1;

        classesSeparator_Section(constraints);
        testAndGroupCallsSeparator_Section(constraints);
        bddSeparator_Section(constraints);
        resetToDefaults_Section(configurable, constraints);

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

        ifShowsSeparator_ForClasses_CheckBox = new JCheckBox("Show separators");
        separatorColor_ForClasses_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForClasses_DarkTheme_ColorPanel = new ColorPanel();
        var disabledColor = UIManager.getColor("Label.disabledForeground");

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.emptyInsets();
        mainPanel.add(sectionLabel("Class separator", true), constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 15, 40);

        @NlsContexts.Label
        JLabel explanation = new JLabel(
                "Draws horizontal line separators above class, enum and extension definitions.");
        explanation.setForeground(disabledColor);
        mainPanel.add(explanation, constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 10, 0);
        mainPanel.add(ifShowsSeparator_ForClasses_CheckBox, constraints);

        // ---

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>classes</b>. <b>Light</b> themes:</html>"));
        panel.add(separatorColor_ForClasses_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, -12, 0);//
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>classes</b>. <b>Dark</b> themes:</html>"));
        panel.add(separatorColor_ForClasses_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);
    }

    private void testAndGroupCallsSeparator_Section(GridBagConstraints constraints) {

        ifShowsSeparator_ForTestOrGroupCalls_CheckBox = new JCheckBox("Show separators");
        separatorColor_ForTestCalls_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForTestCalls_DarkTheme_ColorPanel = new ColorPanel();
        separatorColor_ForGroupCalls_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForGroupCalls_DarkTheme_ColorPanel = new ColorPanel();
        var disabledColor = UIManager.getColor("Label.disabledForeground");

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.emptyInsets();
        mainPanel.add(sectionLabel("Test separator"), constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 5, 40);
        JLabel explanation1 = new JLabel(
                "Draws horizontal line separators above each test and test-group in a test file.");
        explanation1.setForeground(disabledColor);
        mainPanel.add(explanation1, constraints);

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 15, 40);
        JLabel explanation2 = new JLabel("And also for the set up and tear down methods.");
        explanation2.setForeground(disabledColor);
        mainPanel.add(explanation2, constraints);


        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 10, 0);
        mainPanel.add(ifShowsSeparator_ForTestOrGroupCalls_CheckBox, constraints);

        // ---

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>test()</b> calls. <b>Light</b> themes:</html>"));
        panel.add(separatorColor_ForTestCalls_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, -12, 0);//
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>test()</b> calls. <b>Dark</b> themes:</html>"));
        panel.add(separatorColor_ForTestCalls_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>group()</b> calls. <b>Light</b> themes:</html>"));
        panel.add(separatorColor_ForGroupCalls_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insets(10, 20, -12, 0);//
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>group()</b> calls. <b>Dark</b> themes:</html>"));
        panel.add(separatorColor_ForGroupCalls_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);
    }

    private void bddSeparator_Section(GridBagConstraints constraints) {

        ifShowsSeparator_ForBdds_CheckBox = new JCheckBox("Show separators");
        separatorColor_ForBddCalls_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForBddCalls_DarkTheme_ColorPanel = new ColorPanel();
        separatorColor_ForBddKeywords_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForBddKeywords_DarkTheme_ColorPanel = new ColorPanel();
        separatorColor_ForBddComments_LightTheme_ColorPanel = new ColorPanel();
        separatorColor_ForBddComments_DarkTheme_ColorPanel = new ColorPanel();
        var disabledColor = UIManager.getColor("Label.disabledForeground");

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.emptyInsets();
        mainPanel.add(sectionLabel("Bdd separator"), constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 20, 40);
        JLabel explanation = new JLabel(
                "<html>This is for the <b>BDD Framework</b> package: <a href='https://pub.dev/packages/bdd_framework'>pub.dev/packages/bdd_framework</a></html>");
        explanation.setCursor(new Cursor(Cursor.HAND_CURSOR));
        explanation.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e1) {
                try {
                    Desktop.getDesktop().browse(new URI("https://pub.dev/packages/bdd_framework"));
                } catch (IOException | URISyntaxException e2) {
                    e2.printStackTrace();
                }
            }
        });
        mainPanel.add(explanation, constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 5, 40);
        JLabel explanation1 = new JLabel(
                "Draws horizontal line separators above each Bdd in a test file.");
        JLabel explanation2 = new JLabel(
                "<html>And also between Bdd keywords: " +
                        "<b>.given(...)</b> " +
                        "| <b>.when(...)</b> " +
                        "| <b>.then(...)</b> " +
                        "| <b>.example(...)</b>" +
                        "</html>");
        JLabel explanation3 = new JLabel(
                "<html>And also for comments that start with: " +
                        "<b>// Given:</b> " +
                        "| <b>// When:</b> " +
                        "| <b>// Then:</b> " +
                        "| <b>// When/Then:</b>" +
                        "</html>");
        explanation1.setForeground(disabledColor);
        explanation2.setForeground(disabledColor);
        explanation3.setForeground(disabledColor);
        mainPanel.add(explanation1, constraints);
        constraints.gridy++;
        mainPanel.add(explanation2, constraints);
        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 25, 40);
        mainPanel.add(explanation3, constraints);

        // ---

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, 10, 0);
        mainPanel.add(ifShowsSeparator_ForBdds_CheckBox, constraints);

        // ---

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>Bdd()</b> calls. <b>Light</b> themes:</html>"));
        panel.add(separatorColor_ForBddCalls_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insets(0, 20, -12, 0);//
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>Bdd()</b> calls. <b>Dark</b> themes:</html>"));
        panel.add(separatorColor_ForBddCalls_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>given</b> | <b>when</b> | <b>then</b> | <b>example</b> keywords. <b>Light</b> themes:</html>"));
        panel.add(separatorColor_ForBddKeywords_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insets(10, 20, -12, 0);//
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>given</b> | <b>when</b> | <b>then</b> | <b>example</b> keywords. <b>Dark</b> themes:</html>"));
        panel.add(separatorColor_ForBddKeywords_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>Given:</b> | <b>When:</b> | <b>Then:</b> | <b>When/Then:</b> comments. <b>Light</b> themes:</html>"));
        panel.add(separatorColor_ForBddComments_LightTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insets(10, 20, -12, 0);//
        mainPanel.add(panel, constraints);

        // ---

        panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.add(new JLabel("<html>Color for <b>Given:</b> | <b>When:</b> | <b>Then:</b> | <b>When/Then:</b> comments. <b>Dark</b> themes:</html>"));
        panel.add(separatorColor_ForBddComments_DarkTheme_ColorPanel);

        constraints.gridy++;
        constraints.insets = JBUI.insetsLeft(20);
        mainPanel.add(panel, constraints);
    }

    private void resetToDefaults_Section(MarceloPluginConfigurable configurable, GridBagConstraints constraints) {

        constraints.gridy++;
        constraints.insets = JBUI.emptyInsets();
        mainPanel.add(sectionLabel(""), constraints);

        constraints.gridy++;
        constraints.insets = JBUI.emptyInsets();
        var panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton resetButton = new JButton("Reset to defaults");
        resetButton.addActionListener(e -> configurable.resetToDefaults());

        panel.add(resetButton);
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

    public boolean get_IfShowsSeparator_ForTests_CheckBox() {
        return ifShowsSeparator_ForTestOrGroupCalls_CheckBox.isSelected();
    }

    public void set_IfShowsSeparator_ForTests_CheckBox(boolean showSeparators) {
        this.ifShowsSeparator_ForTestOrGroupCalls_CheckBox.setSelected(showSeparators);
    }

    public Color get_SeparatorColor_ForTestCalls_LightTheme_ColorPanel() {
        return separatorColor_ForTestCalls_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForTestCalls_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForTestCalls_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForTestCalls_DarkTheme_ColorPanel() {
        return separatorColor_ForTestCalls_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForTestCalls_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForTestCalls_DarkTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForGroupCalls_LightTheme_ColorPanel() {
        return separatorColor_ForGroupCalls_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForGroupCalls_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForGroupCalls_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForGroupCalls_DarkTheme_ColorPanel() {
        return separatorColor_ForGroupCalls_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForGroupCalls_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForGroupCalls_DarkTheme_ColorPanel.setSelectedColor(color);
    }

    public boolean get_IfShowsSeparator_ForBdds_CheckBox() {
        return ifShowsSeparator_ForBdds_CheckBox.isSelected();
    }

    public void set_IfShowsSeparator_ForBdds_CheckBox(boolean showSeparators) {
        this.ifShowsSeparator_ForBdds_CheckBox.setSelected(showSeparators);
    }

    public Color get_SeparatorColor_ForBddCalls_LightTheme_ColorPanel() {
        return separatorColor_ForBddCalls_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForBddCalls_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForBddCalls_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForBddCalls_DarkTheme_ColorPanel() {
        return separatorColor_ForBddCalls_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForBddCalls_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForBddCalls_DarkTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForBddKeywords_LightTheme_ColorPanel() {
        return separatorColor_ForBddKeywords_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForBddKeywords_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForBddKeywords_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForBddKeywords_DarkTheme_ColorPanel() {
        return separatorColor_ForBddKeywords_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForBddKeywords_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForBddKeywords_DarkTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForBddComments_LightTheme_ColorPanel() {
        return separatorColor_ForBddComments_LightTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForBddComments_LightTheme_ColorPanel(Color color) {
        this.separatorColor_ForBddComments_LightTheme_ColorPanel.setSelectedColor(color);
    }

    public Color get_SeparatorColor_ForBddComments_DarkTheme_ColorPanel() {
        return separatorColor_ForBddComments_DarkTheme_ColorPanel.getSelectedColor();
    }

    public void set_SeparatorColor_ForBddComments_DarkTheme_ColorPanel(Color color) {
        this.separatorColor_ForBddComments_DarkTheme_ColorPanel.setSelectedColor(color);
    }
}

