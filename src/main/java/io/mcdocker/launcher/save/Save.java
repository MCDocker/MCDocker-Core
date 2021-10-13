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
import io.mcdocker.launcher.save.formats.BuildFormat;
import io.mcdocker.launcher.save.formats.ModFormat;

import java.util.List;

public class Save {

    String name;
    String description;
    String version;
    BuildFormat type;
    String author;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public BuildFormat getBuild() { return type; }
    public String getAuthor() { return author; }
    public String getVersion() { return version; }

    public Save(String name, String description, String version, String author, ClientType type, String buildVersion, List<ModFormat> mods) {
        this.name = name;
        this.description = description;
        this.version = version;
        this.type = new BuildFormat(type, buildVersion, mods);
        this.author = author;
    }

}
