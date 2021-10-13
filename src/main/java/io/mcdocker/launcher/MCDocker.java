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

package io.mcdocker.launcher;

import io.mcdocker.launcher.config.Config;
import io.mcdocker.launcher.fx.MainScene;
import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.OSUtils;

import java.io.IOException;

public class MCDocker {

    private static String[] arguments = new String[]{};

    public static void main(String[] args) throws IOException {
        OSUtils.getUserDataFile().mkdir();

        Logger.log("MCDocker starting");

        arguments = args;

        Logger.log("Using Java Version - " + System.getProperty("java.version"));
        Logger.log("MCDocker resources location: " + OSUtils.getUserData());

        Config.getConfig().init();

        Logger.log("Operating System - " + OSUtils.getOperatingSystem().getName());

        Thread shutdownHook = new Thread(MCDocker::shutdown);
        Runtime.getRuntime().addShutdownHook(shutdownHook);
        MainScene.launch(MainScene.class, args);
    }

    public static String[] getArguments() { return arguments; }
    public static String getArgument(String argument) {
        for(String arg : getArguments()) {
            if(arg.split("=")[0].equalsIgnoreCase(argument)) {
                if(arg.split("=").length > 0) return arg.split("=")[1];
                else return arg;
            }
        }
        return "";
    }

    private static void shutdown() {
        Logger.log("MCDocker is shutting down\r\n");
    }

}
