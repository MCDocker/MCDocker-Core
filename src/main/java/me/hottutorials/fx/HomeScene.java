package me.hottutorials.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HomeScene extends VBox {

    public HomeScene() {
        FXMLLoader scene = new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomeScene.fxml"));
        scene.setController(this);
        scene.setRoot(this);

        try {
            scene.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
