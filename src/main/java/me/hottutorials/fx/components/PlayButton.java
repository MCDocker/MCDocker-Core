package me.hottutorials.fx.components;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import me.hottutorials.auth.Authentication;
import me.hottutorials.auth.impl.OfflineAuth;
import me.hottutorials.content.ClientType;
import me.hottutorials.content.Version;
import me.hottutorials.launch.LaunchWrapper;
import me.hottutorials.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class PlayButton extends AnchorPane {

    public PlayButton() {
        FXMLLoader button = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/PlayButton.fxml"));
        button.setController(this);
        button.setRoot(this);

        try {
            LaunchWrapper launchWrapper = new LaunchWrapper(new Version("1.8.9",  8), ClientType.VANILLA);
            Authentication auth = /*new MicrosoftAuth();*/ new OfflineAuth();

            button.load();

            Button btn = (Button) lookup("#playButton");
            String playText = btn.getText();
            btn.setOnAction(actionEvent -> auth.authenticate(btn::setText).thenAccept(account -> launchWrapper.launch(account).thenAcceptAsync(process -> {
                Platform.runLater(() -> {
                    btn.setDisable(true);
                    btn.setText("Launched");
                });
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = input.readLine()) != null) Logger.debug(line);
                    input.close();
                    Platform.runLater(() -> {
                        btn.setDisable(false);
                        btn.setText(playText);
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            })));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
