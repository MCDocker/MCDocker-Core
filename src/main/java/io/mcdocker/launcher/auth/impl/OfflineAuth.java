/*
 * MCDocker, an open source Minecraft launcher.
 * Copyright (C) 2021 MCDocker
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package io.mcdocker.launcher.auth.impl;

import com.google.gson.JsonArray;
import io.mcdocker.launcher.auth.Account;
import io.mcdocker.launcher.auth.Authentication;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class OfflineAuth implements Authentication {

    private final String username;

    public OfflineAuth(String username) {
        this.username = username;
    }

    public OfflineAuth() {
        this("Player");
    }

    @Override
    public CompletableFuture<Account> authenticate(Consumer<String> status) {
        return CompletableFuture.completedFuture(new Account(username, UUID.randomUUID().toString().replace("-", ""), "0", new JsonArray()));
    }

    public String getUsername() {
        return username;
    }

}
