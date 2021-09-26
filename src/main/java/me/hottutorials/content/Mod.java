package me.hottutorials.content;

import com.google.gson.JsonObject;

public class Mod {

    private String name;
    private String description;
    private String icon;
    private Type type;
    private JsonObject data;

    public Mod(String name, String description, String icon, Type type, JsonObject data) {
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.type = type;
        this.data = data;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getIcon() { return icon; }
    public Type getType() { return type; }
    public JsonObject getData() { return data; }

    public enum Type {
        MODRINTH,
        CURSEFORGE,
        EXTERNAL;
    }

}
