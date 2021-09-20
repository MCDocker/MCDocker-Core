package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.gui.components.layout.ListLayout;
import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;
import me.hottutorials.gui.components.layout.ListEntry;
import me.hottutorials.utils.OSUtils;

import javax.swing.*;
import java.util.Arrays;

public class SettingsPanel implements IPanel {
    @Override
    public JPanel init() {
        ListLayout panel = new ListLayout();

        JComboBox themes = new JComboBox(new String[] {"Dark", "Light"});
        themes.setSelectedIndex(0);

        panel.addEntries("General", Arrays.asList(
                new ListEntry("Assets folder", "Used to store logs, containers and more", new JTextField(OSUtils.getUserData()))
        ));

        panel.addEntries("Appearance", Arrays.asList(
                new ListEntry("Theme", "Sets the Theme", themes)
        ));

        panel.update();

        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.SETTINGS;
    }
}
