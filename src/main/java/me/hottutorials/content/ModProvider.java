package me.hottutorials.content;

import me.hottutorials.content.modrinth.ModrinthFilter;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface ModProvider<T extends ModrinthFilter> {

    CompletableFuture<List<Mod>> getMods(T filter);
    default CompletableFuture<List<Mod>> getMods() {
        return getMods(null);
    }

}
