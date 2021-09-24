package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.config.Config;
import me.hottutorials.config.ConfigSerializer;
import me.hottutorials.gui.GUIUtils;
import me.hottutorials.gui.components.layout.ListLayout;
import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;
import me.hottutorials.gui.components.layout.ListEntry;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.StringUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.lang.reflect.Field;
import java.util.*;
import java.util.List;

public class SettingsPanel implements IPanel {
    @Override
    public JPanel init() {
        JPanel container = new JPanel();
        container.setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
        container.setBackground(GUIUtils.background);
        container.setBorder(new EmptyBorder(10, 10, 10, 10));

        ListLayout panel = new ListLayout();
        JScrollPane pane = new JScrollPane(panel);
        pane.getVerticalScrollBar().setUnitIncrement(16);

        panel.setBorder(null);

        updatePanel(panel);

        panel.setBackground(GUIUtils.background);

        JButton resetSettings = new JButton("Reset Settings");
        resetSettings.setSize(150, resetSettings.getHeight());
        resetSettings.setMaximumSize(resetSettings.getSize());
        resetSettings.addActionListener(e -> {
            int output = JOptionPane.showConfirmDialog(container, "Are you sure you want to reset settings?", "Reset Settings", JOptionPane.YES_NO_OPTION);
            if (output == JOptionPane.YES_OPTION) {
                Config.getConfig().saveDefaultSettings();
                updatePanel(panel);
                container.revalidate();
                container.repaint();
                panel.revalidate();
                panel.repaint();
            }
        });

        container.add(pane);
        container.add(resetSettings);

        return container;
    }

    private void updatePanel(ListLayout panel) {
        panel.removeAll();

        Map<String, Map<String, Object>> categories = new HashMap<>();

        for (Field groups : ConfigSerializer.class.getFields()) {
            groups.setAccessible(true);
            String group = groups.getName();
            Map<String, Object> entries = new HashMap<>();
            for (Field field : groups.getType().getFields()) {
                field.setAccessible(true);
                entries.put(field.getName(), field.getType());
            }
            categories.put(group, entries);
        }

        for(Map.Entry<String, Map<String, Object>> category : categories.entrySet()) {
            String categoryName = StringUtils.upperCaseFirstLetterEachWord(category.getKey().replaceAll("_", " "));
            List<ListEntry> entries = new ArrayList<>();
            for(Map.Entry<String, Object> field : category.getValue().entrySet()) {
                JComponent component;

                if (field.getValue() == String.class) component = new JTextField(String.valueOf(Config.getConfig().getObject("config." + category.getKey() + "." + field.getKey())));
                else if(field.getValue() == Boolean.class) component = new JCheckBox();
                else if(field.getValue() == Integer.class || field.getValue() == Double.class) {
                    Double value = Double.valueOf(String.valueOf(Config.getConfig().getObject("config." + category.getKey() + "." + field.getKey())));
                    SpinnerListModel model = new SpinnerListModel();
                    model.setValue(value);
                    component = new JSpinner(model);
                }
                else if(field.getValue() == List.class) component = new JComboBox<>();
                else component = null;

                // TODO: Add i18n support for descriptions names etc

                if(component != null) entries.add(new ListEntry(StringUtils.upperCaseFirstLetterEachWord(field.getKey().replaceAll("_", " ")), "Sadg", component));
            }
            panel.addEntries(categoryName, entries);
        }

        panel.update();
    }

    @Override
    public EPanel getType() {
        return EPanel.SETTINGS;
    }
}
