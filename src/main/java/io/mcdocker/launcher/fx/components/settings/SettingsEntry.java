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

import com.google.gson.JsonElement;
import io.mcdocker.launcher.config.Config;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;

public class SettingsEntry extends AnchorPane {

    private final String name;
    private final String description;
    private final Object value;

    public SettingsEntry(String category, String name, String description, JsonElement value) {
        this.name = name;
        this.description = description;
        this.value = value;

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/settings/SettingsEntry.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();

            Label labelName = (Label) lookup("#settingName");
            Label labelDescription = (Label) lookup("#settingDescription");

            labelName.setText(name);
            labelDescription.setText(description);

//            if(value.getAsJsonPrimitive().isBoolean()) {
//                CheckBox cb = new CheckBox();
//                cb.setSelected(value.getAsBoolean());
//
//                cb.setOnAction((e) -> Config.getConfig().saveOption(category + "." + name, cb.isSelected()));
//
//                getChildren().add(cb);
//                setRightAnchor(cb, 5.00);
//            } else if(value.getAsJsonPrimitive().isString()) {
//                TextField field = new TextField();
//                field.setText(value.getAsString());
//
//                EventHandler<KeyEvent> event = keyEvent -> Config.getConfig().saveOption(category + "." + name, field.getText());
//                field.addEventHandler(KeyEvent.KEY_RELEASED, event);
//
//                getChildren().add(field);
//                setRightAnchor(field, 5.00);
//            }
//            else if(value.getAsJsonPrimitive().isNumber()) {
//                Spinner<Double> spinner = new Spinner<>();
//                // TODO: Do spinner
//            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Object getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
