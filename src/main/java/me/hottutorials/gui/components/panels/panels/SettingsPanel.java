package me.hottutorials.gui.components.panels.panels;

import me.hottutorials.config.Config;
import me.hottutorials.config.ConfigSerializer;
import me.hottutorials.gui.components.layout.ListLayout;
import me.hottutorials.gui.components.panels.EPanel;
import me.hottutorials.gui.components.panels.IPanel;
import me.hottutorials.gui.components.layout.ListEntry;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.StringUtils;

import javax.swing.*;
import java.lang.reflect.Field;
import java.util.*;

public class SettingsPanel implements IPanel {
    @Override
    public JPanel init() {
        ListLayout panel = new ListLayout();

        JComboBox themes = new JComboBox(new String[] {"Dark", "Light"});
        themes.setSelectedIndex(0);

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
                if (field.getValue() == String.class) component = new JTextField();
                else if(field.getValue() == Boolean.class) component = new JCheckBox();
                else if(field.getValue() == Integer.class || field.getValue() == Double.class) component = new JSpinner();
                else if(field.getValue() == List.class) component = new JComboBox<>();
                else component = null;

                // TODO: Add loading from settings file
                // TODO: Add i18n support for descriptions names etc

                if(component != null) entries.add(new ListEntry(StringUtils.upperCaseFirstLetterEachWord(field.getKey().replaceAll("_", " ")), "Sadg", component));
            }
            panel.addEntries(categoryName, entries);
        }

        panel.update();

        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.SETTINGS;
    }
}
