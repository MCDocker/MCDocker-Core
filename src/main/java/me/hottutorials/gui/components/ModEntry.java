package me.hottutorials.gui.components;

import com.google.gson.JsonObject;
import me.hottutorials.content.Mod;
import me.hottutorials.gui.components.layout.ListEntry;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class ModEntry extends JPanel {

    private Mod mod;

    public ModEntry(Mod mod) {
        this.mod = mod;
        render();
    }

    private void render() {
//        setLayout(new BorderLayout());
//
//        JPanel leftContainer = new JPanel();
//        JPanel rightContainer = new JPanel();
//
//        leftContainer.setLayout(new BorderLayout());
//
//        ImageIcon icon = new ImageIcon();
//        if(mod.getIcon() == null) icon.setImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("pack.png"))).getImage());
//        else icon.setImage(new ImageIcon(mod.getIcon()).getImage());
//        Image image = icon.getImage().getScaledInstance(50, 50, Image.SCALE_DEFAULT);
////        JLabel iconLabel = new JLabel(image.);
//        iconLabel.setPreferredSize(new Dimension(50, 50));
//
//        leftContainer.add(iconLabel);
//        add(leftContainer);
    }

}
