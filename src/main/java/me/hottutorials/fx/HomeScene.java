package me.hottutorials.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class HomeScene extends VBox {

    public HomeScene() {
        FXMLLoader sidebar = new FXMLLoader(getClass().getClassLoader().getResource("fxml/HomeScene.fxml"));
        sidebar.setController(this);
        sidebar.setRoot(this);

        try {
            sidebar.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
