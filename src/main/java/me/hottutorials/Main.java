package me.hottutorials;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import me.hottutorials.config.Config;
import me.hottutorials.fx.MainScene;
import me.hottutorials.gui.MainWindow;
import me.hottutorials.utils.Logger;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.StringUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class Main {

    public static void main(String[] args) throws Exception {
        OSUtils.getUserDataFile().mkdir();

        Logger.log("MCDocker starting");
        Logger.log("Using Java Version - " + System.getProperty("java.version"));
        Logger.log("MCDocker resources location: " + OSUtils.getUserData());

        Config.getConfig().init();

        Logger.log("Operating System - " + OSUtils.getOperatingSystem().getName());

        Thread shutDownHook = new Thread(Main::shutDownMethod);
        Runtime.getRuntime().addShutdownHook(shutDownHook);

        MainScene.launch(MainScene.class, args);

    }

    private static void shutDownMethod() {
        Logger.log("MCDocker is shutting down\r\n");
    }

}
