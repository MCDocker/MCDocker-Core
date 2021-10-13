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

package io.mcdocker.launcher.content;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class Mod {

    private final String name;
    private final String description;
    private final ClientType type;
    private final String link;
    private final String author;
    private final String icon;

    public Mod(String name, String description, ClientType type, String link, String author, String icon) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.link = link;
        this.author = author;
        this.icon = icon;
    }

    public String getName() { return name; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getLink() { return link; }
    public ClientType getType() { return type; }
    public abstract CompletableFuture<Set<ModVersion>> getVersions();

    @Override
    public String toString() {
        return "Mod{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
