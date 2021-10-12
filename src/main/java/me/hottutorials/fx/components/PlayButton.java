package me.hottutorials.fx.components;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import me.hottutorials.auth.Account;
import me.hottutorials.auth.Authentication;
import me.hottutorials.auth.impl.OfflineAuth;
import me.hottutorials.content.ClientType;
import me.hottutorials.content.Version;
import me.hottutorials.launch.LaunchWrapper;
import me.hottutorials.utils.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.ExecutionException;

public class PlayButton extends AnchorPane {

    public PlayButton() {
        FXMLLoader button = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/PlayButton.fxml"));
        button.setController(this);
        button.setRoot(this);

        try {
            String version = "1.8.9";
            Authentication auth = /*new MicrosoftAuth();*/ new OfflineAuth();

            button.load();

            Button btn = (Button) lookup("#playButton");
            String playText = "PLAY " + version;
            btn.setText(playText);
            btn.setOnAction(actionEvent -> Version.getVersion(version).thenAcceptAsync(v -> {
                try {
                    if (v.isEmpty()) return;
                    LaunchWrapper launchWrapper = new LaunchWrapper(v.get(), ClientType.VANILLA);
                    Account account = auth.authenticate(btn::setText).get();
                    Process process = launchWrapper.launch(account).get();
                    Platform.runLater(() -> {
                        btn.setDisable(true);
                        btn.setText("LAUNCHED");
                    });
                    BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    String line;
                    while ((line = input.readLine()) != null) Logger.debug(line);
                    input.close();
                    Platform.runLater(() -> {
                        btn.setDisable(false);
                        btn.setText(playText);
                    });
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
