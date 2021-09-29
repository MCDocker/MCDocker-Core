package me.hottutorials.fx.components;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Sidebar extends VBox {

    public Sidebar() {
        FXMLLoader sidebar = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/Sidebar.fxml"));
        sidebar.setController(this);
        sidebar.setRoot(this);

        try {
            sidebar.load();
//            Button button = (Button) lookup("#");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
