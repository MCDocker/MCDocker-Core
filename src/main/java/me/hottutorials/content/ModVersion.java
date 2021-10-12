package me.hottutorials.content;

import java.util.concurrent.CompletableFuture;

public abstract class ModVersion {

    private final String name;

    public ModVersion(String name) {
        this.name = name;
    }

    public abstract CompletableFuture<String> getDownloadUrl();

}
