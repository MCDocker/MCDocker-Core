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

package io.mcdocker.launcher.config;

import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.OSUtils;
import me.grison.jtoml.impl.Toml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Config {

    private final File file = new File(OSUtils.getUserData() + "config.toml");
    public File getFile() { return file; }

    private static final Config INSTANCE = new Config();
    public static Config getConfig() { return INSTANCE; }

    public void overwriteSettings(ConfigSerializer conf) {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(Toml.serialize("config", conf));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getObject(String name) {
        return deepSearch(name, Toml.parse(getSettings()));
    }

    public Object deepSearch(String name, Toml obj) {
        List<String> path = new LinkedList<>(Arrays.asList(name.split("\\.")));
        if(path.size() == 1) {
            return obj.get(path.get(0));
        } else {

            Object newValue = obj.get(path.get(0));
            path.remove(0);
            StringBuilder builder = new StringBuilder();
            path.forEach(s -> builder.append(s).append("."));
            builder.deleteCharAt(builder.toString().length() - 1).toString();
            return deepSearch(builder.toString(), Toml.parse(Toml.serialize(newValue)));
        }
    }

    public void init() {
        Logger.log("Initiating config");
        try {
            if(!Config.getConfig().getFile().exists()) {
                Config.getConfig().getFile().createNewFile();
                saveDefaultSettings();
                Logger.log("Created new config");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigSerializer getSerializedSettings() {
        return Toml.parse(file).getAs("config", ConfigSerializer.class);
    }

    public String getSettings() {
        return Toml.parse(file).serialize();
    }

    public ConfigSerializer getDefaultSettings() {
        return new ConfigSerializer();
    }

    public void saveDefaultSettings() {
        try {
            FileWriter writer = new FileWriter(file);
            writer.write(Toml.serialize("config", getDefaultSettings()));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
