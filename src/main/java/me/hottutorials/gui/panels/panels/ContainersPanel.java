package me.hottutorials.gui.panels.panels;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.hottutorials.content.Mod;
import me.hottutorials.gui.components.ModEntry;
import me.hottutorials.gui.components.layout.ListLayout;
import me.hottutorials.gui.panels.EPanel;
import me.hottutorials.gui.panels.IPanel;
import me.hottutorials.utils.Constants;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

import javax.swing.*;
import java.awt.*;

public class ContainersPanel implements IPanel {
    @Override
    public JPanel init() {
        JPanel panel = new JPanel();
        JPanel cards = new JPanel(new CardLayout());

        ListLayout list = new ListLayout();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        JsonObject mods = gson.fromJson(RequestBuilder.getBuilder()
                .setURL(Constants.URLs.MODRINTH_API.getURL() + "api/v1/mod/mOgUt4GM")
                .setMethod(Method.GET)
                .send(), JsonObject.class);

        panel.add(new ModEntry(new Mod("test", "test", null, Mod.Type.EXTERNAL, null)));

        panel.add(cards);
        return panel;
    }

    @Override
    public EPanel getType() {
        return EPanel.CONTAINERS;
    }
}
