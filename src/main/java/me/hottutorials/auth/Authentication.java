package me.hottutorials.auth;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public interface Authentication<T extends Authentication<T>> {

    CompletableFuture<Account> authenticate(Consumer<String> status);
    default CompletableFuture<Account> authenticate() {
        return authenticate(status -> {});
    }

}
