package me.hottutorials.content;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

public class MinVersion {

    private final String name;
    private final Supplier<CompletableFuture<Version>> version;

    public MinVersion(String name, Supplier<CompletableFuture<Version>> version) {
        this.name = name;
        this.version = version;
    }

    public String getName() {
        return name;
    }

    public Supplier<CompletableFuture<Version>> get() {
        return version;
    }

}
