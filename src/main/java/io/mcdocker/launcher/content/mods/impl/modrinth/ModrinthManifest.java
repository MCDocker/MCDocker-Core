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

import io.mcdocker.launcher.content.ClientType;
import io.mcdocker.launcher.content.mods.ModManifest;

public class ModrinthManifest extends ModManifest {

    private String id;
    private String description;
    private ClientType clientType;
    private String url;
    private String author;
    private String iconUrl;

    public ModrinthManifest(String id, String name, String description, ClientType clientType, String url, String author, String iconUrl, String type) {
        super(name, type);
        this.id = id;
        this.description = description;
        this.clientType = clientType;
        this.url = url;
        this.author = author;
        this.iconUrl = iconUrl;
    }

    public ModrinthManifest(String id, String name, String description, ClientType clientType, String url, String author, String iconUrl) {
        this(id, name, description, clientType, url, author, iconUrl, ModrinthMod.class.getName());
    }

    public String getUrl() {
        return url;
    }

    public String getDescription() {
        return description;
    }

    public String getAuthor() {
        return author;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public ClientType getClientType() {
        return clientType;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setClientType(ClientType clientType) {
        this.clientType = clientType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

}
