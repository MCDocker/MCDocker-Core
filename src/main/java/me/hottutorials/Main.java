package me.hottutorials;

import me.hottutorials.config.Config;
import me.hottutorials.content.modrinth.Modrinth;
import me.hottutorials.fx.MainScene;
import me.hottutorials.utils.Logger;
import me.hottutorials.utils.OSUtils;

public class Main {

    private static String[] arguments = new String[]{};

    public static void main(String[] args) {
        OSUtils.getUserDataFile().mkdir();

        Logger.log("MCDocker starting");

        arguments = args;

        Logger.log("Using Java Version - " + System.getProperty("java.version"));
        Logger.log("MCDocker resources location: " + OSUtils.getUserData());

        Config.getConfig().init();

        Logger.log("Operating System - " + OSUtils.getOperatingSystem().getName());

        Thread shutDownHook = new Thread(Main::shutDownMethod);
        Runtime.getRuntime().addShutdownHook(shutDownHook);

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

    private static void shutDownMethod() {
        Logger.log("MCDocker is shutting down\r\n");
    }

}
