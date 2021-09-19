package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;

import javax.swing.*;

public class SkinsPanel implements IPanel {
    @Override
    public JPanel init() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Skins Panel"));
        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.CONTAINERS;
    }
}
