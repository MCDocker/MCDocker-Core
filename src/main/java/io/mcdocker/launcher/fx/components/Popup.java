/*
 *
 *   MCDocker, an open source Minecraft launcher.
 *   Copyright (C) 2021 MCDocker
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.mcdocker.launcher.fx.components;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Popup extends BorderPane {

    public Popup(String title, String content) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/Popup.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void display(Scene scene) {
        Overlay overlay = (Overlay) scene.getRoot().lookup("#overlay");
        overlay.setCenter(this);
        overlay.show();
        overlay.setPadding(new Insets(100));
    }

}
