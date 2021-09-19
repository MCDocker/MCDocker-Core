package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.gui.components.layout.ListLayout;
import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;
import me.hottutorials.gui.components.layout.ListEntry;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.Arrays;

public class SettingsPanel implements IPanel {
    @Override
    public JPanel init() {
        ListLayout panel = new ListLayout();

        panel.addEntries("test", Arrays.asList(
                new ListEntry("Test", "Nice test ngl lol", new JTextField("nice")),
                new ListEntry("awnawawawawaw", "Nice test ngl lol", new JTextField("nice")),
                new ListEntry("nawa", "Nice test ngl lol", new JTextField("n")))
        );

        panel.update();

        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.SETTINGS;
    }
}
