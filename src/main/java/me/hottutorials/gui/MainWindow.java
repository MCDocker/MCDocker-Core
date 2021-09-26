package me.hottutorials.gui;

import com.formdev.flatlaf.FlatDarkLaf;
import me.hottutorials.gui.panels.PanelManager;
import me.hottutorials.gui.components.sidebar.Sidebar;
import me.hottutorials.utils.Logger;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class MainWindow {

    private final JFrame frame = new JFrame();
    public JFrame getFrame() { return frame; }

    private static final MainWindow INSTANCE = new MainWindow();
    public static MainWindow getInstance() { return INSTANCE; }

    private final JPanel panel = new JPanel();
    public JPanel getPanel() { return panel; }

    public void init() {
        Logger.log("Initiating Main Window");
        Logger.log("Initiating FlatDark Theme");
        FlatDarkLaf.setup();
        Logger.log("Initiated FlatDark Theme");

        frame.setPreferredSize(new Dimension(900, 600));
        frame.setLocation((Toolkit.getDefaultToolkit().getScreenSize().width / 2) - 450, Toolkit.getDefaultToolkit().getScreenSize().height / 2 - 300);
        frame.setTitle("MCDocker");
        frame.setIconImage(new ImageIcon(Objects.requireNonNull(getClass().getClassLoader().getResource("img/iconWithBackgroundRound.png"))).getImage());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//        frame.setResizable(false);

        panel.setLayout(new BorderLayout());
        panel.setBackground(GUIUtils.background);

        PanelManager.getInstance().init();
        JPanel cards = new JPanel(new CardLayout());
        PanelManager.getInstance().addPanelsToCard(cards);

        panel.add(new Sidebar(frame, cards), BorderLayout.WEST);
        panel.add(cards);

        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
        Logger.log("Initiated Main Window");
    }

}
