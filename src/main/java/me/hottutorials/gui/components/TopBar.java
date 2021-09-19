package me.hottutorials.gui.components;

import me.hottutorials.gui.GUIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class TopBar extends JPanel {

    public TopBar(JFrame frame) {
        setPreferredSize(new Dimension((int) frame.getPreferredSize().getWidth(), 40));
        setBackground(GUIUtils.backgroundDarkDark);
        setLayout(new BorderLayout());

        JLabel titleLabel = new JLabel(frame.getTitle());
        titleLabel.setForeground(Color.white);
        titleLabel.setHorizontalAlignment(JLabel.CENTER);
        titleLabel.setVerticalAlignment(JLabel.CENTER);
        add(titleLabel, BorderLayout.CENTER);

        JPanel btnContainer = new JPanel();
        btnContainer.setLayout(new BorderLayout());
        btnContainer.setBorder(new EmptyBorder(10, 10, 10, 10));
        btnContainer.setBackground(GUIUtils.backgroundDarkDark);

        JButton closeBtn = new JButton("x");
        closeBtn.setBackground(Color.red);
        closeBtn.addActionListener(e -> System.exit(0));
        btnContainer.add(closeBtn, BorderLayout.EAST);

        JButton minimizeBtn = new JButton("-");
        minimizeBtn.addActionListener(e -> frame.setExtendedState(JFrame.ICONIFIED));
        btnContainer.add(minimizeBtn, BorderLayout.WEST);

        add(btnContainer, BorderLayout.EAST);

        GUIUtils.makeDraggableFrame(this, frame);
    }

}
