/*
 *
 *   MCDocker, an open source Minecraft launcher.
 *   Copyright (C) 2021 MCDocker
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.mcdocker.launcher.content.clients.impl.fabric;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.clients.ClientDetails;
import io.mcdocker.launcher.content.clients.ClientProvider;
import io.mcdocker.launcher.content.clients.impl.vanilla.Vanilla;
import io.mcdocker.launcher.content.clients.impl.vanilla.VanillaClient;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class Fabric implements ClientProvider<FabricClient> {

    private static final Gson gson = new Gson();

    @Override
    public CompletableFuture<List<ClientDetails<FabricClient>>> getClients() {
        return CompletableFuture.supplyAsync(() -> {
            List<ClientDetails<FabricClient>> clients = new ArrayList<>();
            JsonArray versionArray = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL("https://meta.fabricmc.net/v2/versions/game")
                    .setMethod(Method.GET)
                    .send(), JsonArray.class);
            StreamSupport.stream(versionArray.spliterator(), false)
                    .map(JsonElement::getAsJsonObject)
                    .forEach(client -> clients.add(new ClientDetails<>(client.get("version").getAsString(), () -> CompletableFuture.supplyAsync(() -> {
                        String loaderUrl = "https://meta.fabricmc.net/v2/versions/loader/" + client.get("version").getAsString();
                        JsonObject loaderData = gson.fromJson(RequestBuilder.getBuilder()
                                .setURL(loaderUrl)
                                .setMethod(Method.GET)
                                .send(), JsonArray.class).get(0).getAsJsonObject();

                        String dataUrl = "https://meta.fabricmc.net/v2/versions/loader/" + client.get("version").getAsString() + "/" + loaderData.getAsJsonObject("loader").get("version").getAsString() + "/profile/json";

                        JsonObject clientData = gson.fromJson(RequestBuilder.getBuilder()
                                .setURL(dataUrl)
                                .setMethod(Method.GET)
                                .send(), JsonObject.class);

                        Vanilla vanilla = new Vanilla();
                        VanillaClient vanillaClient = null;
                        try {
                            vanillaClient = vanilla.getClient(clientData.get("inheritsFrom").getAsString()).get().get();
                        } catch (InterruptedException | ExecutionException e) {
                            e.printStackTrace();
                        }
                        if(vanillaClient == null) return null;

                        return new FabricClient(dataUrl, clientData, loaderData.getAsJsonObject("loader").get("version").getAsString(), vanillaClient.getManifest());
                    }))));
            return clients;
        });
    }

    @Override
    public CompletableFuture<Optional<FabricClient>> getClient(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getClients().get().stream().filter(client -> client.getName().equalsIgnoreCase(name)).findFirst().map(client -> {
                    try {
                        return client.get().get().get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                        return null;
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
                return Optional.empty();
            }
        });
    }

}
