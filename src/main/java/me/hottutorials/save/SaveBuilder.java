package me.hottutorials.save;

import me.hottutorials.save.formats.BuildFormat;
import me.hottutorials.save.formats.ClientTypes;
import me.hottutorials.save.formats.ModFormat;

import java.util.ArrayList;
import java.util.List;

public class SaveBuilder {

    private String name;
    private String description;
    private String author;
    private String version;
    private ClientTypes type;
    private final List<ModFormat> mods = new ArrayList<>();
    private String buildVersion;

    public SaveBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SaveBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SaveBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public SaveBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public SaveBuilder setBuildType(ClientTypes type) {
        this.type = type;
        return this;
    }

    public SaveBuilder setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
        return this;
    }

    public SaveBuilder addMod(String name, String version, String link) {
        mods.add(new ModFormat(name, version, link));
        return this;
    }

    public SaveBuilder addMod(String name, String version) {
        addMod(name, version, null);
        return this;
    }

    public Save build() {
        return new Save(name, description, version, author, type, buildVersion, mods);
    }

}
