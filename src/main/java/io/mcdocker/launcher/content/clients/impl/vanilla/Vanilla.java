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

package io.mcdocker.launcher.content.clients.impl.vanilla;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.clients.ClientDetails;
import io.mcdocker.launcher.content.clients.ClientProvider;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class Vanilla implements ClientProvider<VanillaClient> {

    private static final Gson gson = new Gson();

    @Override
    public CompletableFuture<List<ClientDetails<VanillaClient>>> getClients() {
        return CompletableFuture.supplyAsync(() -> {
            List<ClientDetails<VanillaClient>> clients = new ArrayList<>();
            JsonArray versionArray = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL("https://launchermeta.mojang.com/mc/game/version_manifest.json")
                    .setMethod(Method.GET)
                    .send(), JsonObject.class).get("versions").getAsJsonArray();
            StreamSupport.stream(versionArray.spliterator(), false)
                    .map(JsonElement::getAsJsonObject)
                    .forEach(client -> clients.add(new ClientDetails<>(client.get("id").getAsString(), () -> CompletableFuture.supplyAsync(() -> {
                        String dataUrl = client.get("url").getAsString();
                        JsonObject clientData = gson.fromJson(RequestBuilder.getBuilder()
                                .setURL(dataUrl)
                                .setMethod(Method.GET)
                                .send(), JsonObject.class);
                        return new VanillaClient(dataUrl, clientData);
                    }))));
            return clients;
        });
    }

    @Override
    public CompletableFuture<Optional<VanillaClient>> getClient(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getClients().get().stream().filter(client -> client.getName().equalsIgnoreCase(name)).findFirst().map(client -> {
                    try {
                        return client.get().get().get();
                    } catch (InterruptedException | ExecutionException e) {
                        return null;
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                return Optional.empty();
            }
        });
    }

}
