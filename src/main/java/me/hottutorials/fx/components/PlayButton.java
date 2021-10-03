package me.hottutorials.fx.components;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import me.hottutorials.content.modrinth.Modrinth;
import me.hottutorials.launch.Authenticator;

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
//                System.out.println(Modrinth.getMods());
                new Notification("Test", "test", Notification.NotificationType.ERROR);
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
