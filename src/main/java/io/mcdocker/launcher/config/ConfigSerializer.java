package io.mcdocker.launcher.config;

public class ConfigSerializer {

    public ConfigCategory generalCategory = new ConfigCategory("General",
            new ConfigSetting("Discord Presence", "Displays your MCDocker activity on Discord", false, "CheckBox")
    );

    public ConfigCategory launchCategory = new ConfigCategory("Launch Category",
            new ConfigSetting("JVM Arguments", "Custom arguments when launching Minecraft", "", "TextField", true)
    );

}
