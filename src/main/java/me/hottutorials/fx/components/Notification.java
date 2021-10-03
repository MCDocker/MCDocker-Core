package me.hottutorials.fx.components;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class Notification extends BorderPane {

    @FXML
    private Button closeNotiButton;

    public enum NotificationType {
        INFORMATION,
        WARNING,
        ERROR;
    }

    public Notification(String title, String content, NotificationType type) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/Notification.fxml"));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            loader.setController(this);

            Stage stage = new Stage();
            stage.setScene(scene);
            stage.setHeight(80);
            stage.setWidth(300);
            stage.setResizable(false);
            stage.setAlwaysOnTop(true);
            stage.initStyle(StageStyle.UNDECORATED);

            double sWidth = Screen.getPrimary().getBounds().getWidth();
            double sHeight = Screen.getPrimary().getBounds().getHeight();

            stage.setX(sWidth - stage.getWidth() - 10);
            stage.setY(sHeight - stage.getHeight() - 10);

            Button button = (Button) loader.getNamespace().get("closeNotiButton");
            button.setOnAction((e) -> stage.close());

            FontIcon icon = (FontIcon) loader.getNamespace().get("fontIcon");
            switch (type) {
                case INFORMATION:
                    icon.setIconLiteral("bi-info");
                    break;
                case ERROR:
                    icon.setIconLiteral("bi-x");
                    break;
                case WARNING:
                    icon.setIconLiteral("bi-exclamation-triangle-fill");
                    break;
            }

            Label titleLabel = (Label) loader.getNamespace().get("titleLabel");
            Label contentLabel = (Label) loader.getNamespace().get("contentLabel");
            titleLabel.setText(title);
            contentLabel.setText(content);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
