package me.hottutorials.gui.components.panels;

import me.hottutorials.gui.GUIUtils;
import me.hottutorials.gui.MainWindow;
import me.hottutorials.gui.components.panels.panels.ContainersPanel;
import me.hottutorials.gui.components.panels.panels.HomePanel;
import me.hottutorials.gui.components.panels.panels.SettingsPanel;
import me.hottutorials.gui.components.panels.panels.SkinsPanel;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class PanelManager {

    private final List<IPanel> panels = new ArrayList<>();
    public List<IPanel> getPanels() { return panels; }

    private static final PanelManager INSTANCE = new PanelManager();
    public static PanelManager getInstance() { return INSTANCE; }

    public void init() {
        register(new HomePanel());
//        register(new SkinsPanel());
        register(new ContainersPanel());
        register(new SettingsPanel());
    }

    public void register(IPanel panel) {
        if(panels.contains(panel)) return;
        panels.add(panel);
    }

    public IPanel getDefaultPanel() {
        for(IPanel panel : panels) {
            if(panel.getType().equals(EPanel.HOME)) {
                return panel;
            }
        }
        return panels.get(0);
    }

    public void addPanelsToCard(JPanel cards) {
        for(IPanel ipanel : panels) {
            JPanel panel = ipanel.init();
            panel.setBackground(GUIUtils.background);
            cards.add(panel, ipanel.getType().name());
        }
    }

}
