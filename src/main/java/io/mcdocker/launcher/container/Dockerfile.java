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

package io.mcdocker.launcher.container;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.clients.ClientManifest;
import io.mcdocker.launcher.content.mods.ModManifest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Dockerfile {

    private static final Gson gson = new Gson();

    private UUID id = UUID.randomUUID();

    private String name = "Untitled";
    private JsonObject client;
    private List<ModManifest> mods = new ArrayList<>();

    @Deprecated
    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JsonObject getClient() {
        return client;
    }

    public <T extends ClientManifest> T getClient(Class<T> ofType) {
        return gson.fromJson(client, ofType);
    }

    public void setClient(JsonObject client) {
        this.client = client;
    }

    public <T extends ClientManifest> void setClient(T client) {
        setClient((JsonObject) gson.toJsonTree(client));
    }

    public List<ModManifest> getMods() {
        return mods;
    }

    public void setMods(List<ModManifest> mods) {
        this.mods = mods;
    }

}
