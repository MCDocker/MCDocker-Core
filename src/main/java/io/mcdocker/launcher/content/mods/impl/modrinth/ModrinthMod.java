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
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.ClientType;
import io.mcdocker.launcher.content.mods.Mod;
import io.mcdocker.launcher.content.mods.ModVersion;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class ModrinthMod extends Mod<ModrinthManifest> {

    private static final Gson gson = new Gson();
    private static final String URL = "https://api.modrinth.com";

    public ModrinthMod(JsonObject data) {
        super(new ModrinthManifest(
                data.get("mod_id").getAsString(),
                null,
                data.get("title").getAsString(),
                data.get("description").getAsString(),
                ClientType.CUSTOM, // TODO: Make this not custom.
                data.get("page_url").getAsString(),
                data.get("author").getAsString(),
                data.get("icon_url").getAsString()
        ));
    }

    public ModrinthMod(ModrinthManifest manifest) {
        super(manifest);
    }

    @Override
    public CompletableFuture<Set<ModVersion>> getVersions() {
        return CompletableFuture.supplyAsync(() -> {
            JsonObject response = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL(URL + "/api/v1/mod/" + manifest.getId().replace("local-", "")) // Why do I have to do this??
                    .setMethod(Method.GET)
                    .send(true), JsonObject.class);
            Set<ModVersion> versions = new HashSet<>();
            for (JsonElement v : response.get("versions").getAsJsonArray()) {
                String version = v.getAsString();
                versions.add(new ModVersion(version) {
                    @Override
                    public CompletableFuture<String> getDownloadUrl() {
                        return CompletableFuture.supplyAsync(() -> {
                            JsonObject response = gson.fromJson(RequestBuilder.getBuilder()
                                    .setURL(URL + "/api/v1/version/" + version)
                                    .setMethod(Method.GET)
                                    .send(true), JsonObject.class);
                            JsonArray files = response.get("files").getAsJsonArray();
                            for (JsonElement f : files) {
                                JsonObject file = f.getAsJsonObject();
                                if (file.get("primary").getAsBoolean()) return file.get("url").getAsString();
                            }
                            return files.get(0).getAsJsonObject().get("url").getAsString();
                        });
                    }
                });
            }
            return versions;
        });
    }

}
