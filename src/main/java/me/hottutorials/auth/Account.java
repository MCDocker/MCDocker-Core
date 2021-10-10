package me.hottutorials.auth;

import com.google.gson.JsonArray;

public class Account<T extends Authentication<T>> {

    private final T authMethod;
    private final String username;
    private final String uuid;
    private final JsonArray skins;

    public Account(T authMethod, String username, String uuid, JsonArray skins) {
        this.authMethod = authMethod;
        this.username = username;
        this.uuid = uuid;
        this.skins = skins;
    }

    public String getUsername() {
        return username;
    }

    public String getUniqueId() {
        return uuid;
    }

    public JsonArray getSkins() {
        return skins;
    }

    public T getAuthMethod() {
        return authMethod;
    }

}
