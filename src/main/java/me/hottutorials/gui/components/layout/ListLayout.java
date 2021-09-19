package me.hottutorials.gui.components.layout;

import me.hottutorials.gui.GUIUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListLayout extends JPanel {

    private final Map<String, List<ListEntry>> lists = new HashMap<>();

    public ListLayout() {
        update();
    }

    public void update() {
        setLayout(new FlowLayout());
        setBorder(new EmptyBorder(10, 10, 10, 10));

        for(Map.Entry<String, List<ListEntry>> entry : lists.entrySet()) {
            JPanel category = new JPanel(new BorderLayout());
            category.setBorder(new EmptyBorder(10, 10, 10, 10));
            category.setBackground(GUIUtils.backgroundDark);

            JLabel title = new JLabel(entry.getKey());
            title.setFont(new Font(title.getFont().getFontName(), Font.BOLD, 20));
            title.setBorder(new EmptyBorder(4, 4, 4, 4));

            category.add(title, BorderLayout.NORTH);

            JPanel categoryEntries = new JPanel();
            categoryEntries.setLayout(new BoxLayout(categoryEntries, BoxLayout.Y_AXIS));

            for(ListEntry listEntry : getEntries(entry.getKey())) {
                categoryEntries.add(listEntry);
            }

            category.add(categoryEntries, BorderLayout.SOUTH);
            add(category);

        }
    }

    public ListLayout addEntry(String group, ListEntry listEntry) {
        List<ListEntry> listEntries = lists.get(group);
        listEntries.add(listEntry);
        lists.put(group, listEntries);
        return this;
    }

    public ListLayout addEntries(String group, List<ListEntry> listEntries) {
        lists.put(group, listEntries);
        return this;
    }

    public List<ListEntry> getEntries(String group) {
        List<ListEntry> listEntries = new ArrayList<>();

        for(Map.Entry<String, List<ListEntry>> entry : lists.entrySet()) {
            if(entry.getKey().equalsIgnoreCase(group)) {
                listEntries.addAll(entry.getValue());
            }
        }

        return listEntries;
    }

}
