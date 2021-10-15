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

package io.mcdocker.launcher.fx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class MainScene extends Application {

    private static final List<Region> panelsList = new ArrayList<>();

    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainScene.fxml"));
        Scene scene = new Scene(root);
        root.getStyleClass().add("dark-mode");
        root.setStyle("-fx-background-color: #474747;");

        registerPanels();

        stage.setWidth(900.0);
        stage.setHeight(600.0);
        stage.setMinWidth(900.0);
        stage.setMinHeight(600.0);
        stage.setTitle("MCDocker");
        stage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("img/iconInvertedNoBackground.png"), 128, 128, true, true));
        stage.setScene(scene);

        stage.show();

        StackPane panels = (StackPane) scene.lookup("#panelContainer");

        for (Region panel : panelsList) {
            panel.setVisible(false);
            if(panel.getClass() == HomeScene.class) panel.setVisible(true);
            panels.getChildren().add(panel);
        }
    }

    public static List<Region> getPanelsList() {
        return panelsList;
    }

    private void registerPanels() {
        register(new HomeScene());
        register(new ContainersScene());
        register(new SettingsScene());
    }

    private void register(Region panel) {
        if (panelsList.contains(panel)) return;
        panelsList.add(panel);
    }


}
