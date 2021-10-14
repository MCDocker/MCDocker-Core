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

package io.mcdocker.launcher.fx.components.settings;

import io.mcdocker.launcher.utils.StringUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class SettingsGroup extends VBox {

    private final List<SettingsEntry> entries;


    public SettingsGroup(String name, List<SettingsEntry> entries) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/settings/SettingsGroup.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        this.entries = entries;

        try {
            loader.load();

            Label title = (Label) lookup("#groupTitle");
            title.setText(StringUtils.uppercaseFirstLetterOfString(name));

            for(SettingsEntry entry : entries) {
                getChildren().add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public Label getTitle() {return title;}
//    public List<SettingsEntry> getEntries() {return entries;}
}
