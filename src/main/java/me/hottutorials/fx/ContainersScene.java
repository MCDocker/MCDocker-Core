package me.hottutorials.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.hottutorials.content.Mod;
import me.hottutorials.fx.components.ContainerEntry;

import java.io.IOException;

public class ContainersScene extends ScrollPane {

    public ContainersScene() {
        FXMLLoader scene = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ContainersScene.fxml"));
        scene.setController(this);
        scene.setRoot(this);

        try {
            scene.load();

            VBox container = (VBox) scene.getNamespace().get("contContainer");
            for(int i = 0; i < 5; i++) {
                container.getChildren().add(new ContainerEntry());
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
