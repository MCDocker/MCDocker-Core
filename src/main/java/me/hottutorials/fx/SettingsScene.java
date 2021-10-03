package me.hottutorials.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SettingsScene extends VBox {

    public SettingsScene() {
        FXMLLoader scene = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SettingsScene.fxml"));
        scene.setController(this);
        scene.setRoot(this);

        try {
            scene.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
