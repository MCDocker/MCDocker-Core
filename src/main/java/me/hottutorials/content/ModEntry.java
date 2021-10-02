package me.hottutorials.content;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ModEntry extends VBox {

    @FXML
    private Label title;

    private final Mod mod;

    public ModEntry(Mod mod) {
        this.mod = mod;
        handle();
    }

    private void handle() {
        FXMLLoader entry = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/ModEntry.fxml"));
        entry.setController(this);
        entry.setRoot(this);

        try {
            entry.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void initialize() {
        title.setText(mod.getName());
    }

}
