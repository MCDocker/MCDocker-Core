package me.hottutorials.config;

import me.hottutorials.utils.OSUtils;

public class ConfigSerializer {

    public Appearance appearance = new Appearance();
    public General general = new General();

    public static class Appearance {
        public String theme = "dark";
    }

    public static class General {
        public String resourcesPath = OSUtils.getUserData();
        public String minecraftPath = OSUtils.getMinecraftPath();
    }

}
