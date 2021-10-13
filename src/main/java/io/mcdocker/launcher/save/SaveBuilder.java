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

package io.mcdocker.launcher.save;

import io.mcdocker.launcher.content.ClientType;
import io.mcdocker.launcher.save.formats.ModFormat;

import java.util.ArrayList;
import java.util.List;

public class SaveBuilder {

    private String name;
    private String description;
    private String author;
    private String version;
    private ClientType type;
    private final List<ModFormat> mods = new ArrayList<>();
    private String buildVersion;

    public SaveBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SaveBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SaveBuilder setAuthor(String author) {
        this.author = author;
        return this;
    }

    public SaveBuilder setVersion(String version) {
        this.version = version;
        return this;
    }

    public SaveBuilder setBuildType(ClientType type) {
        this.type = type;
        return this;
    }

    public SaveBuilder setBuildVersion(String buildVersion) {
        this.buildVersion = buildVersion;
        return this;
    }

    public SaveBuilder addMod(String name, String version, String link) {
        mods.add(new ModFormat(name, version, link));
        return this;
    }

    public SaveBuilder addMod(String name, String version) {
        addMod(name, version, null);
        return this;
    }

    public Save build() {
        return new Save(name, description, version, author, type, buildVersion, mods);
    }

}
