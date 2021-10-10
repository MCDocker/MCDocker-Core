package me.hottutorials.content;

import java.util.concurrent.CompletableFuture;

public abstract class Version {

    private final String id;

    public Version(String id) {
        this.id = id;
    }

    public abstract CompletableFuture<String> getDownloadUrl();

    public String getId() {
        return id;
    }

}
