package io.mcdocker.launcher.config;

public class ConfigSerializer {

    public GeneralCategory general = new GeneralCategory();
    public LaunchCategory launch = new LaunchCategory();

    public class GeneralCategory {
        public boolean DiscordRPC = true;
    }

    public class LaunchCategory {
        public String Custom_JVM_Arguments = "";
    }
}
