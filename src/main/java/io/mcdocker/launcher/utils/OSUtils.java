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

import java.io.File;

public class OSUtils {

    public static String getUserData() {
        return System.getProperty("user.home") + File.separator + ".mcdocker/";
    }

    public static File getUserDataFile() {
        return new File(OSUtils.getUserData());
    }

    public static String getMinecraftPath() {
        switch (getOperatingSystem()) {
            case WINDOWS:
                return System.getenv("APPDATA") + File.separator + ".minecraft/";
            case LINUX:
                return System.getProperty("user.home") + File.separator + ".minecraft/";
            case MACOS:
                return System.getProperty("user.home") + File.separator + "Library" + File.separator + "Application Support" + File.separator + "minecraft/";
            default:
                return null;
        }
    }

    public enum OperatingSystem {
        WINDOWS("Windows"),
        MACOS("MacOS"),
        LINUX("Linux"),
        OTHER("Other");

        OperatingSystem(String name) {
            this.name = name;
        }

        private String name;
        public String getName() {
            return name;
        }
    }
    public static OperatingSystem getOperatingSystem() {
        String os = System.getProperty("os.name");

        switch (os.toLowerCase()) {
            default:
                return OperatingSystem.OTHER;
            case "windows 10":
                return OperatingSystem.WINDOWS;
            case "linux":
            case "nux":
            case "unix":
                return OperatingSystem.LINUX;
            case "darwin":
            case "mac":
                return OperatingSystem.MACOS;
        }

    }
    public static String getArchitecture() {
        return System.getProperty("os.arch");
    }
    public static boolean isWindows() {
        return getOperatingSystem() == OperatingSystem.WINDOWS;
    }
    public static boolean isLinux() {
        return getOperatingSystem() == OperatingSystem.LINUX;
    }
    public static boolean isMacOS() {
        return getOperatingSystem() == OperatingSystem.MACOS;
    }

}
