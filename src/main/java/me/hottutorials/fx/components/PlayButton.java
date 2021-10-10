package me.hottutorials.fx.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import me.hottutorials.content.ClientType;
import me.hottutorials.launch.LaunchWrapper;

import java.io.IOException;

public class PlayButton extends AnchorPane {

    public PlayButton() {
        FXMLLoader button = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/PlayButton.fxml"));
        button.setController(this);
        button.setRoot(this);

        try {
            button.load();

            Button btn = (Button) lookup("#playButton");
            btn.setOnAction(actionEvent -> {
                new LaunchWrapper("1.8.9", ClientType.VANILLA);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
