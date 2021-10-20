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

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.config.Config;
import io.mcdocker.launcher.config.ConfigCategory;
import io.mcdocker.launcher.config.ConfigSerializer;
import io.mcdocker.launcher.config.ConfigSetting;
import io.mcdocker.launcher.fx.components.ContainerEntry;
import io.mcdocker.launcher.fx.components.settings.SettingsEntry;
import io.mcdocker.launcher.fx.components.settings.SettingsGroup;
import io.mcdocker.launcher.utils.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingsScene extends ScrollPane {

    public SettingsScene() {
        FXMLLoader scene = new FXMLLoader(getClass().getClassLoader().getResource("fxml/SettingsScene.fxml"));
        scene.setController(this);
        scene.setRoot(this);

        try {
            scene.load();

            JsonObject config = Config.getConfig().getConfigJson();
            VBox settingsList = (VBox) scene.getNamespace().get("settingsContainer");
            for(Map.Entry<String, JsonElement> obj : config.entrySet()) {

                if(!config.has(obj.getKey()) || !config.get(obj.getKey()).isJsonObject()) {
                    Logger.err("Not a valid setting");
                    continue;
                }

                if(!config.getAsJsonObject(obj.getKey()).has("settings")) {
                    continue;
                }

                String category = config.getAsJsonObject(obj.getKey()).get("name").getAsString();

                List<SettingsEntry> entries = new ArrayList<>();
                for(Map.Entry<String, JsonElement> settings : obj.getValue().getAsJsonObject().entrySet()) {
                    if(settings.getValue().isJsonArray()) {
                        for(JsonElement setting : settings.getValue().getAsJsonArray()) {
                            ConfigSetting stg = Config.getConfig().getSetting(setting.getAsJsonObject().get("name").getAsString(), Config.getConfig().getCategory(category));

                            Class<?> clazz = Class.forName(stg.getNode());
                            Node node = (Node) clazz.newInstance();

                            if(stg.isAdvanced() && !Config.getConfig().deepSearch("advanced", Config.getConfig().getConfigJson()).getAsJsonObject().get("value").getAsBoolean()) continue;

                            entries.add(new SettingsEntry(category, setting.getAsJsonObject().get("name").getAsString(), stg.getDescription(), setting.getAsJsonObject().get("value"), node));
                        }
                    }
                }

                if(entries.isEmpty()) continue;

                settingsList.getChildren().add(new SettingsGroup(category, entries));
            }

        } catch (IOException | ClassNotFoundException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

}
