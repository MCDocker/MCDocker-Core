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

package io.mcdocker.launcher.utils;

import java.util.Set;

public enum OperatingSystem {

    WINDOWS("Windows", "windows 7", "windows 10"),
    MACOS("MacOS", "mac", "darwin"),
    LINUX("Linux", "linux", "nux", "unix"),
    OTHER("Other");

    private final String name;
    private final Set<String> properties;

    public static OperatingSystem OS = get(System.getProperty("os.name"));

    OperatingSystem(String name, String... properties) {
        this.name = name;
        this.properties = Set.of(properties);
    }

    public String getName() {
        return name;
    }

    public Set<String> getProperties() {
        return properties;
    }

    public static OperatingSystem get(String os) {
        for (OperatingSystem operatingSystem : values()) if (operatingSystem.properties.contains(os.toLowerCase())) return operatingSystem;
        return OTHER;
    }

}
