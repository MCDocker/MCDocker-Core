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

public class ModFormat {

    String name;
    String version;
    String link;

    public ModFormat(String name, String version, String link) {
        this.name = name;
        this.version = version;
        this.link = link;
    }

    public String getCombinedString() {
        String newName = name.replaceAll(" ", "-").replaceAll(";", "");
        String newVersion = version.replaceAll(" ", "-").replaceAll(";", "");

        String newLink = link != null
                ? link.replaceAll("https", "").replaceAll("http", "").replaceAll("://", "")
                : "custom";

        return newName + ";" + newVersion + ";" + newLink;
    }

    public String getNameFromString(String str) {
        String[] strings = str.split(";");
        if(strings.length != 3) return null;
        return strings[0];
    }

    public String getVersionFromString(String str) {
        String[] strings = str.split(";");
        if(strings.length != 3) return null;
        return strings[0];
    }

    public String getLinkFromString(String str) {
        String[] strings = str.split(";");
        if(strings.length != 3) return null;
        return strings[0];
    }

}
