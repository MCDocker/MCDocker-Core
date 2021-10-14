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

package io.mcdocker.launcher.content.mods.impl.modrinth;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.mods.ModDetails;
import io.mcdocker.launcher.content.mods.ModProvider;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Modrinth implements ModProvider<ModrinthFilter, ModrinthMod> {

    private static final Gson gson = new Gson();
    private static final String URL = "https://api.modrinth.com";

    @Override
    public CompletableFuture<List<ModDetails<ModrinthMod>>> getMods(ModrinthFilter filter) {
        return CompletableFuture.supplyAsync(() -> {
            JsonObject response = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL(URL + "/api/v1/mod" + (filter == null ? "" : "?" + filter.getQuery()))
                    .setMethod(Method.GET)
                    .send(true), JsonObject.class);
            List<ModDetails<ModrinthMod>> mods = new ArrayList<>();
            for (JsonElement hit : response.get("hits").getAsJsonArray()) {
                JsonObject data = hit.getAsJsonObject();
                mods.add(getMod(data, "mod_id"));
            }
            return mods;
        });
    }

    @Override
    public CompletableFuture<ModDetails<ModrinthMod>> getMod(String id) {
        return CompletableFuture.supplyAsync(() -> {
            String response = RequestBuilder.getBuilder()
                    .setURL(URL + "/api/v1/mod/" + id)
                    .setMethod(Method.GET)
                    .send(true);
            if (response == null) return null;
            return getMod(gson.fromJson(response, JsonObject.class), "id");
        });
    }

    private ModDetails<ModrinthMod> getMod(JsonObject data, String idKey) {
        String name = data.get("title").getAsString();
        return new ModDetails<>(name, data.get(idKey).getAsString().replace("local-", "")) {
            @Override
            public CompletableFuture<ModrinthMod> getVersion(String version) {
                return CompletableFuture.supplyAsync(() -> {
                    JsonObject response = gson.fromJson(RequestBuilder.getBuilder()
                            .setURL(URL + "/api/v1/version/" + version)
                            .setMethod(Method.GET)
                            .send(true), JsonObject.class);
                    return new ModrinthMod(name, response);
                });
            }
        };
    }

}
