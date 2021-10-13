package me.hottutorials.auth;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface EmailPasswordAuthentication extends Authentication {

    CompletableFuture<Account> authenticate(String email, String password, Consumer<String> status);
    default CompletableFuture<Account> authenticate(String email, String password) {
        return authenticate(email, password, status -> {});
    }

}
