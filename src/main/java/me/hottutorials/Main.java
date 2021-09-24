package me.hottutorials;

import com.google.gson.JsonArray;
import me.grison.jtoml.impl.Toml;
import me.hottutorials.config.Config;
import me.hottutorials.config.ConfigBuilder;
import me.hottutorials.config.ConfigSerializer;
import me.hottutorials.gui.MainWindow;
import me.hottutorials.utils.Logger;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.Request;
import me.hottutorials.utils.http.RequestBuilder;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        OSUtils.getUserDataFile().mkdir();

        Logger.log("MCDocker starting");
        Logger.log("Using Java Version - " + System.getProperty("java.version"));
        Logger.log("MCDocker resources location: " + OSUtils.getUserData());

        Config.getConfig().init();

        Logger.log("Operating System - " + OSUtils.getOperatingSystem().getName());

        Thread shutDownHook = new Thread(Main::shutDownMethod);
        Runtime.getRuntime().addShutdownHook(shutDownHook);

        MainWindow window = new MainWindow();
        window.init();
    }

    private static void shutDownMethod() {
        Logger.log("MCDocker is shutting down\r\n");
    }

}
