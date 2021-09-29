package me.hottutorials.utils;

public class Constants {

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
