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

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ContainerEntry extends Button {

    public ContainerEntry() {
        FXMLLoader entry = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/ContainerEntry.fxml"));
        entry.setController(this);
        entry.setRoot(this);

        try {
            entry.load();

            Label title = (Label) entry.getNamespace().get("contTitle");
            Label description = (Label) entry.getNamespace().get("contDesc");
            ImageView icon = (ImageView) entry.getNamespace().get("contIconView");

//            title.setText(mod.getName());
//            description.setText(mod.getDescription());
//
//            if(mod.getIcon() != null) {
//                Image image = new Image(mod.getIcon());
//                icon.setImage(image);
//            } else {
//                Image image = new Image(String.valueOf(getClass().getClassLoader().getResource("img/pack.png")));
//                icon.setImage(image);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
