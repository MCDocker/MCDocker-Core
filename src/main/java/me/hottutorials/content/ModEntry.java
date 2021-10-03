package me.hottutorials.content;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ModEntry extends Button {

    private final Mod mod = new Mod.ModProperties().setName("a").setDescription("agawg").getMod();

    public ModEntry() {
        FXMLLoader entry = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/ModEntry.fxml"));
        entry.setController(this);
        entry.setRoot(this);

//        Label title = (Label) entry.getNamespace().get("modTitle");
//        Label desc = (Label) entry.getNamespace().get("modDesc");
//
//        title.setText(mod.getName());
//        desc.setText(mod.getDescription());

        try {
            entry.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
