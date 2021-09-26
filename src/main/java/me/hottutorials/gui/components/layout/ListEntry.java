package me.hottutorials.gui.components.layout;

import me.hottutorials.config.ConfigSerializer;
import me.hottutorials.gui.GUIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class ListEntry extends JPanel {

    private final String name;
    private final String description;
    private final JComponent component;

    @Override
    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public ListEntry(String name, String description, JComponent component) {
        this.name = name;
        this.description = description;
        this.component = component;
        render();
    }

    public void render() {
        setLayout(new BorderLayout());

        JPanel leftContainer = new JPanel(new BorderLayout());
        JLabel nameLabel = new JLabel(name);
        nameLabel.setFont(new Font(nameLabel.getFont().getFontName(), Font.PLAIN, 15));
        leftContainer.add(nameLabel, BorderLayout.NORTH);
        JLabel descriptionLabel = new JLabel(description);
        descriptionLabel.setForeground(new Color(255, 255, 255, 60));
        descriptionLabel.setBorder(new EmptyBorder(0, 2, 0, 2));

        leftContainer.add(descriptionLabel, BorderLayout.SOUTH);
        leftContainer.setBackground(GUIUtils.backgroundDarkDark);

        component.setPreferredSize(new Dimension(300, 50));

        add(leftContainer, BorderLayout.WEST);
        add(component, BorderLayout.EAST);

        setPreferredSize(new Dimension(600, 60));
        setBackground(GUIUtils.backgroundDarkDark);
        setBorder(new EmptyBorder(10, 10, 10, 10));
    }

}
