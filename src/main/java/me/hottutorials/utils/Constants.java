package me.hottutorials.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

public class Constants {

    private final static Gson gson = new GsonBuilder().create();

    private static JsonObject getLatestMC() {
            return gson.fromJson(RequestBuilder.getBuilder().setURL("https://launchermeta.mojang.com/mc/game/version_manifest.json").setMethod(Method.GET).send(), JsonObject.class).getAsJsonObject("latest");
    }

    public enum URLs {
        CURSEFORGE_API("https://addons-ecs.forgesvc.net/api/v2/"),
        MODRINTH_API("https://api.modrinth.com/"),
        MOJANG_API("https://api.mojang.com/"),
        MOJANG_STATUS("https://status.mojang.com/check"),
        MOJANG_AUTH("https://authserver.mojang.com");

        private String url;
        public String getURL() { return url; };

        URLs(String url) { this.url = url; }
    }


}
