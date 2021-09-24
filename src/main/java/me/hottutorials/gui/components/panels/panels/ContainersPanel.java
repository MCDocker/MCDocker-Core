package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;

import javax.swing.*;
import java.awt.*;

public class ContainersPanel implements IPanel {
    @Override
    public JPanel init() {
        JPanel panel = new JPanel();
        JPanel cards = new JPanel(new CardLayout());



        panel.add(cards);
        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.CONTAINERS;
    }
}
