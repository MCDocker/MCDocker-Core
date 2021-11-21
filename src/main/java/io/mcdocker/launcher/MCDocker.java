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

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import io.javalin.Javalin;
import io.javalin.core.util.JavalinLogger;
import io.mcdocker.launcher.auth.AccountsManager;
import io.mcdocker.launcher.config.Config;
import io.mcdocker.launcher.discord.Discord;
import io.mcdocker.launcher.utils.Folders;
import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.OperatingSystem;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

public class MCDocker {

    private static String[] arguments = new String[]{};
    private final static String version = "0.1.0";
    private static Discord discord;
    private static Javalin javalin;

    private static final String APIUrl = "http://localhost:6167/api/";

    public static void main(String[] args) throws IOException {
        Folders.USER_DATA.mkdirs();

        Logger.log("MCDocker starting");

        arguments = args;

        Logger.log("Using Java Version - " + System.getProperty("java.version"));
        Logger.log("MCDocker resources location: " + Folders.USER_DATA);
        Logger.log("Operating System - " + OperatingSystem.OS.getName());

        Config.getConfig().init();
        AccountsManager.getInstance().init();

        Thread shutdownHook = new Thread(MCDocker::shutdown);
        Runtime.getRuntime().addShutdownHook(shutdownHook);

        initDiscord();
        initArgs();

        startServer();
    }

    private static void initArgs() {
        Logger.log("Initiating Arguments");
    }
    public static void initDiscord() {
        Discord.init();
        try {
            CreateParams params = new CreateParams();
            params.setFlags(CreateParams.Flags.NO_REQUIRE_DISCORD);
            params.setClientID(889845849578962964L);

            discord = new Discord(new Core(params));
            CompletableFuture.runAsync(discord::start);
        } catch (RuntimeException ignored) { Logger.err("Could not initiate Discord SDK. Is the Discord Client running?"); }
    }

    public static String getArgument(String argument) {
        for(String arg : arguments) {
            if(arg.split("=")[0].equalsIgnoreCase(argument)) {
                if(arg.split("=").length != 1) return arg.split("=")[1];
                else return arg;
            }
        }
        return null;
    }
    public static Discord getDiscord() {
        return discord;
    }
    public static String getVersion() { return version; }
    public static Javalin getJavalin() { return javalin; }

    public static String getAPIUrl() {return APIUrl;}

    public static void startServer() {
        Logger.log("Starting server on port 5005");
        JavalinLogger.enabled = false;
        javalin = Javalin.create().start(5005);
    }

    private static void shutdown() {
        Logger.log("MCDocker is shutting down\r\n");
    }

}
