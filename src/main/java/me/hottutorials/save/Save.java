package me.hottutorials.save;

import me.hottutorials.save.formats.BuildFormat;
import me.hottutorials.save.formats.ClientTypes;
import me.hottutorials.save.formats.ModFormat;

import java.util.List;

public class Save {

    String name;
    String description;
    String version;
    BuildFormat type;
    String author;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public BuildFormat getBuild() { return type; }
    public String getAuthor() { return author; }
    public String getVersion() { return version; }

    public Save(String name, String description, String version, String author, ClientTypes type, String buildVersion, List<ModFormat> mods) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.type = new BuildFormat(type, buildVersion, mods);
        this.author = author;
    }

}
