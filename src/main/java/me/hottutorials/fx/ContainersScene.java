package me.hottutorials.fx;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import me.hottutorials.content.ClientType;
import me.hottutorials.content.Mod;
import me.hottutorials.content.ModEntry;

import java.io.IOException;

public class ContainersScene extends ScrollPane {

    public ContainersScene() {
        FXMLLoader scene = new FXMLLoader(getClass().getClassLoader().getResource("fxml/ContainersScene.fxml"));
        scene.setController(this);
        scene.setRoot(this);

//        VBox list = (VBox) lookup("#containersScene");
//        list.getChildren().add(new ModEntry(Mod.ModProperties.getBuilder().setName("LOL").setLink("ok").setDescription("a").setType(ClientType.FORGE).getMod()));
//        setContent(list);

        try {
            scene.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
