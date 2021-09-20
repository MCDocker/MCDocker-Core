package me.hottutorials.utils;

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
            case "win":
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
