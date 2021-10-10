package me.hottutorials.auth;

import com.google.gson.JsonArray;

public class Account {

    private final String username;
    private final String uuid;
    private final String accessToken;
    private final JsonArray skins;

    public Account(String username, String uuid, String accessToken, JsonArray skins) {
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.skins = skins;
    }

    public String getUsername() {
        return username;
    }

    public String getUniqueId() {
        return uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public JsonArray getSkins() {
        return skins;
    }

}
