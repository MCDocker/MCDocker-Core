package me.hottutorials.save.formats;

import me.hottutorials.content.ClientType;

import java.util.ArrayList;
import java.util.List;

public class BuildFormat {

    String version;
    List<String> mods;
    String type;

    public List<String> getMods() { return mods; }
    public String getType() { return type; }
    public String getVersion() { return version; }

    public BuildFormat(ClientType type, String version, List<ModFormat> mods) {

        List<String> modStrings = new ArrayList<>();

        mods.forEach(modFormat -> modStrings.add(modFormat.getCombinedString()));

        this.version = version;
        this.mods = modStrings;
        this.type = type.name().toLowerCase();
    }


}
