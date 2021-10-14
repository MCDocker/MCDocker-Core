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

package io.mcdocker.launcher.content.mods.impl.curseforge;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.Filter;
import io.mcdocker.launcher.content.mods.ModDetails;
import io.mcdocker.launcher.content.mods.ModProvider;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class CurseForge implements ModProvider<Filter, CurseForgeMod> {

    private static final Gson gson = new Gson();
    private static final String URL = "https://addons-ecs.forgesvc.net/api/v2/addon/";

    @Override
    public CompletableFuture<List<ModDetails<CurseForgeMod>>> getMods(Filter filter) {
        return CompletableFuture.supplyAsync(() -> {
            JsonArray response = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL(URL + "search?gameId=432&sectionId=6") // TODO: Create filter class.
                    .setMethod(Method.GET)
                    .send(true), JsonArray.class);
            List<ModDetails<CurseForgeMod>> mods = new ArrayList<>();
            for (JsonElement mod : response) {
                JsonObject data = mod.getAsJsonObject();
                mods.add(getMod(data));
            }
            return mods;
        });
    }

    @Override
    public CompletableFuture<ModDetails<CurseForgeMod>> getMod(String id) {
        return CompletableFuture.supplyAsync(() -> {
            String response = RequestBuilder.getBuilder()
                    .setURL(URL + id)
                    .setMethod(Method.GET)
                    .send(true);
            if (response == null) return null;
            return getMod(gson.fromJson(response, JsonObject.class));
        });
    }

    private ModDetails<CurseForgeMod> getMod(JsonObject data) {
        String name = data.get("name").getAsString();
        return new ModDetails<>(name, data.get("id").getAsString()) {
            @Override
            public CompletableFuture<CurseForgeMod> getVersion(String version) {
                return CompletableFuture.supplyAsync(() -> {
                    JsonObject response = gson.fromJson(RequestBuilder.getBuilder()
                            .setURL(URL + getId() + "/file/" + version)
                            .setMethod(Method.GET)
                            .send(true), JsonObject.class);
                    return new CurseForgeMod(name, response);
                });
            }
        };
    }

}
