package me.hottutorials.launch;

import com.google.gson.JsonArray;

public class MCProfile {

    private final String name;
    private final String uuid;
    private final JsonArray skins;

    public MCProfile(String name, String uuid, JsonArray skins) {
        this.name = name;
        this.uuid = uuid;
        this.skins = skins;
    }

    public String getName() { return name; }
    public JsonArray getSkins() { return skins; }
    public String getUuid() { return uuid; }
}
