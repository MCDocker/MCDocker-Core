package io.mcdocker.launcher.config;

import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

public class ConfigSerializer {

    public ConfigSetting advanced = new ConfigSetting("advanced", "", false, CheckBox.class.getName());

    public ConfigCategory generalCategory = new ConfigCategory("General",
            new ConfigSetting("Discord Presence", "Displays your MCDocker activity", false, CheckBox.class.getName())
    );

    public ConfigCategory launchCategory = new ConfigCategory("Launch Category",
            new ConfigSetting("JVM Arguments", "A", "", TextField.class.getName(), true)
    );

}
