package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;

import javax.swing.*;

public class HomePanel implements IPanel {
    @Override
    public JPanel init() {
        JPanel panel = new JPanel();
        panel.add(new JLabel("Home Panel"));
        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.HOME;
    }
}
