/*
 * MCDocker, an open source Minecraft launcher.
 * Copyright (C) 2021 MCDocker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.mcdocker.launcher.fx.components;

import javafx.animation.PauseTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import org.kordamp.ikonli.javafx.FontIcon;

import java.io.IOException;

import static io.mcdocker.launcher.utils.FXUtils.editFx;

public class Notification extends BorderPane {

    @FXML
    private Button closeNotiButton;
    private Stage stage;
    private Scene scene;

    public enum NotificationType {
        INFORMATION,
        WARNING,
        ERROR;
    }

    public Notification(String title, String content) {
        this(title, content, NotificationType.INFORMATION);
    }

    public Notification(String title, String content, NotificationType type) {
        editFx(() -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/Notification.fxml"));
                Parent root = loader.load();
                root.getStyleClass().add("dark-mode");
                root.setStyle("-fx-background-color: #474747;");
                this.scene = new Scene(root);
                loader.setController(this);

                this.stage = new Stage();
                stage.setScene(scene);
                stage.setResizable(false);
                stage.setAlwaysOnTop(true);
                stage.initStyle(StageStyle.UNDECORATED);

                double sWidth = Screen.getPrimary().getBounds().getWidth();
                double sHeight = Screen.getPrimary().getBounds().getHeight();

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
                Text contentLabel = (Text) loader.getNamespace().get("contentLabel");
                titleLabel.setText(title);
                contentLabel.setText(content);
                contentLabel.setWrappingWidth(100);

                stage.setHeight(stage.getMinHeight() + contentLabel.getLayoutBounds().getHeight());
                stage.setMinHeight(80);
                stage.setWidth(300);

                stage.setX(sWidth - stage.getWidth() - 10);
                stage.setY(sHeight - stage.getHeight() - 10);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void display() {
        display(Duration.seconds(5));
    }

    public void display(Duration duration) {
        editFx(() -> {
            PauseTransition delay = new PauseTransition(duration);
            delay.setOnFinished((e) -> {
                if (stage.isShowing()) remove();
            });
            delay.play();

            scene.setOnMouseEntered(mouseEvent -> delay.pause());
            scene.setOnMouseExited(mouseEvent -> delay.play());

            stage.show();
        });
    }

    public void remove() {
        stage.close();
    }

}
