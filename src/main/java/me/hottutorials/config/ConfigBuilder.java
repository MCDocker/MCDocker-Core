package me.hottutorials.config;

public class ConfigBuilder {

    private final ConfigSerializer config;

    public ConfigBuilder(ConfigSerializer config) {
        this.config = config;
    }

    public ConfigBuilder() {
        this.config = new ConfigSerializer();
    }

    public ConfigBuilder setAppearance(ConfigSerializer.Appearance appearance) {
        config.appearance = appearance;
        return this;
    }

    public ConfigBuilder setGeneral(ConfigSerializer.General general) {
        config.general = general;
        return this;
    }

    public ConfigSerializer build() {
        return config;
    }

}
