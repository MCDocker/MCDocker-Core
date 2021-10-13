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

import io.mcdocker.launcher.fx.ContainersScene;
import io.mcdocker.launcher.fx.HomeScene;
import io.mcdocker.launcher.fx.MainScene;
import io.mcdocker.launcher.fx.SettingsScene;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class Sidebar extends VBox {

    public Sidebar() {
        FXMLLoader sidebar = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/Sidebar.fxml"));
        sidebar.setController(this);
        sidebar.setRoot(this);

        try {
            sidebar.load();

            Button home = (Button) lookup("#homeSidebarButton");
            Button containers = (Button) lookup("#containersSidebarButton");
            Button settings = (Button) lookup("#settingsSidebarButton");

            containers.setOnAction((e) -> setActivePanel(containers, ContainersScene.class));
            home.setOnAction((e) -> setActivePanel(home, HomeScene.class));
            settings.setOnAction((e) -> setActivePanel(settings, SettingsScene.class));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActivePanel(Button button, Class clazz) {
        for(Region panel : MainScene.getPanelsList()) {
            panel.setVisible(false);
//            button.getStyleClass().clear();
//            button.getStyleClass().add("sidebar-button");
            if(panel.getClass() == clazz) {
                panel.setVisible(true);
//                button.getStyleClass().add("active-sidebar-button");
            }
        }
    }

}
