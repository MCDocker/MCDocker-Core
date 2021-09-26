package me.hottutorials.gui.components.sidebar;

import me.hottutorials.gui.GUIUtils;
import me.hottutorials.gui.panels.EPanel;

import javax.swing.*;
import java.awt.*;

public class SidebarButton extends JButton {

    public SidebarButton(EPanel type, JPanel panel) {
        setText(type.name());
        setBackground(GUIUtils.backgroundDark);
        setForeground(Color.white);
        setFocusPainted(false);
        setBorder(null);
        setAlignmentX(Component.CENTER_ALIGNMENT);
        setSize(500, 50);
        setMaximumSize(getSize());
    }
}
