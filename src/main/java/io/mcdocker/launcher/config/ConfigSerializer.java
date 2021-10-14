package io.mcdocker.launcher.config;

public class ConfigSerializer {

    public GeneralCategory general = new GeneralCategory();
    public LaunchCategory launch = new LaunchCategory();

    private class GeneralCategory {
        public String test = "ok";
    }

    private class LaunchCategory {
        public boolean hm = false;
    }
}
