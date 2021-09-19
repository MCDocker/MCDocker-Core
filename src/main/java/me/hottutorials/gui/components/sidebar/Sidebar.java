package me.hottutorials.gui.components.sidebar;

import me.hottutorials.gui.GUIUtils;
import me.hottutorials.gui.MainWindow;
import me.hottutorials.gui.components.panels.EPanel;

import javax.swing.*;
import java.awt.*;

public class Sidebar extends JPanel {

    public Sidebar(JFrame frame, JPanel cards) {
        setPreferredSize(new Dimension(200, frame.getHeight()));
        setBackground(GUIUtils.backgroundDark);

        BoxLayout layout = new BoxLayout(this, BoxLayout.Y_AXIS);

        setLayout(layout);

        for(EPanel type : EPanel.values()) {
            SidebarButton button = new SidebarButton(type, this);
            button.addActionListener(e -> {
                CardLayout card = (CardLayout) cards.getLayout();
                card.show(cards, type.name());
            });
            add(button);
        }

    }

}
