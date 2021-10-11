package me.hottutorials.auth.impl;

import com.google.gson.JsonArray;
import me.hottutorials.auth.Account;
import me.hottutorials.auth.Authentication;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class OfflineAuth implements Authentication {

    private final String username;

    public OfflineAuth(String username) {
        this.username = username;
    }

    public OfflineAuth() {
        this("TypeScripter");
    }

    @Override
    public CompletableFuture<Account> authenticate(Consumer<String> status) {
        return CompletableFuture.completedFuture(new Account(username, UUID.randomUUID().toString().replace("-", ""), "0", new JsonArray()));
    }

    public String getUsername() {
        return username;
    }

}
