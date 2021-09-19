package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.gui.MainWindow;
import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;

import javax.swing.*;

public class SettingsPanel implements IPanel {
    @Override
    public JPanel init() {
        JPanel panel = new JPanel();
        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.SETTINGS;
    }
}
