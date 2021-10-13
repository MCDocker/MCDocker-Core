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

package io.mcdocker.launcher.save.formats;

import io.mcdocker.launcher.content.ClientType;

import java.util.ArrayList;
import java.util.List;

public class BuildFormat {

    String version;
    List<String> mods;
    String type;

    public List<String> getMods() { return mods; }
    public String getType() { return type; }
    public String getVersion() { return version; }

    public BuildFormat(ClientType type, String version, List<ModFormat> mods) {

        List<String> modStrings = new ArrayList<>();

        mods.forEach(modFormat -> modStrings.add(modFormat.getCombinedString()));

        this.version = version;
        this.mods = modStrings;
        this.type = type.name().toLowerCase();
    }


}
