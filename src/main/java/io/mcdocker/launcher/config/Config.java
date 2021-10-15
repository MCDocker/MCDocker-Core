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

package io.mcdocker.launcher.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.mcdocker.launcher.utils.Folders;
import io.mcdocker.launcher.utils.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Config {

    private static final Config INSTANCE = new Config();
    public static Config getConfig() {return INSTANCE;}

    private final File configFile = new File(Folders.USER_DATA, "config.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void init() throws IOException {
        Logger.log("Initiating Config");
        if(!configFile.exists()) {
            configFile.createNewFile();
            save(new ConfigSerializer());
            Logger.log("Created new config");
        }
        Logger.log("Loading config from '" + configFile.getPath() + "'");
    }

    public void save(ConfigSerializer cfg) {
        try {
            FileWriter writer = new FileWriter(configFile);

            writer.write(gson.toJson(cfg));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save(JsonObject cfg) {
        try {
            FileWriter writer = new FileWriter(configFile);

            writer.write(gson.toJson(cfg));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigSerializer getConfigSerialized() {
        try {
            JsonReader reader = new JsonReader(new FileReader(configFile)   );
            return gson.fromJson(reader, ConfigSerializer.class);
        } catch (FileNotFoundException e) {
            Logger.err("Config file not found");
            save(new ConfigSerializer());
            return new ConfigSerializer();
        }
    }

    public JsonObject getConfigJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader(configFile));
            return gson.fromJson(reader, JsonObject.class);
        } catch (FileNotFoundException e) {
            Logger.err("Config file not found");
            save(new ConfigSerializer());
            return gson.fromJson(gson.toJson(new ConfigSerializer()), JsonObject.class);
        }
    }

    public Object getValue(String name) {
        return deepSearch(name, getConfigJson());
    }

    public void saveOption(String name, Object value) {
        JsonElement element = deepSearch(name, getConfigJson());
        if(element == null) return;

        try {
            JsonReader reader = new JsonReader(new FileReader(configFile));
            JsonObject config = gson.fromJson(reader, JsonObject.class);

            if(value instanceof String) config.getAsJsonObject(name.split("\\.")[0]).addProperty(name.split("\\.")[1], String.valueOf(value));
            else if(value instanceof Boolean) config.getAsJsonObject(name.split("\\.")[0]).addProperty(name.split("\\.")[1], Boolean.valueOf(String.valueOf(value)));
            else if(value instanceof Number) config.getAsJsonObject(name.split("\\.")[0]).addProperty(name.split("\\.")[1], Double.valueOf(String.valueOf(value)));

            save(config);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }


    public JsonElement deepSearch(String name, JsonObject obj) {
        try {
            List<String> path = new LinkedList<>(Arrays.asList(name.split("\\.")));
            if(path.size() == 1) {
                return obj.get(path.get(0));
            } else {

                Object newValue = obj.get(path.get(0));
                path.remove(0);
                StringBuilder builder = new StringBuilder();
                path.forEach(s -> builder.append(s).append("."));
                builder.deleteCharAt(builder.toString().length() - 1).toString();
                return deepSearch(builder.toString(), gson.fromJson(gson.toJson(newValue), JsonObject.class));
            }
        } catch (Exception e) {
            return null;
        }
    }

}
