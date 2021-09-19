package me.hottutorials;

import me.hottutorials.gui.MainWindow;
import me.hottutorials.utils.Logger;
import me.hottutorials.utils.OSUtils;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        OSUtils.getUserDataFile().mkdir();

        Logger.log("MCDocker starting");
        Logger.log("MCDocker resources location: " + OSUtils.getUserData());
        Logger.log("Using Java Version - " + String.class.getPackage().getImplementationVersion() + " - " + String.class.getPackage().getSpecificationVersion());
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
