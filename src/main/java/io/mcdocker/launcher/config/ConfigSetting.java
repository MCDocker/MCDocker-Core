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

import javafx.scene.Node;

public class ConfigSetting {

    private final String name;
    private final Object value;
    private transient final String description;
    private transient final String node;
    private transient final boolean advanced;

    public ConfigSetting(String name, String description, Object value, String node) {
        this.name = name;
        this.description = description;
        this.node = node;
        this.advanced = false;
        this.value = value;
    }

    public ConfigSetting(String name, String description, Object value, String node, Boolean advanced) {
        this.name = name;
        this.description = description;
        this.node = node;
        this.advanced = advanced;
        this.value = value;
    }

    public String getDescription() {return description;}
    public String getName() { return name; }
    public Class<?> getNode() throws ClassNotFoundException { return Class.forName(node); }
    public boolean isAdvanced() {return advanced;}
}
