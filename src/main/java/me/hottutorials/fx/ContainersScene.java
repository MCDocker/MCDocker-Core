package me.hottutorials.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.hottutorials.content.Mod;
import me.hottutorials.fx.components.ModEntry;

import java.io.IOException;

public class ContainersScene extends ScrollPane {

    public ContainersScene() {
        FXMLLoader scene = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ContainersScene.fxml"));
        scene.setController(this);
        scene.setRoot(this);

        try {
            scene.load();

            VBox container = (VBox) scene.getNamespace().get("modsContainer");
            for(int i = 0; i < 5; i++) {
                container.getChildren().add(new ModEntry(new Mod.ModProperties().setName("test " + i).setDescription("cool " + i).getMod()));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
