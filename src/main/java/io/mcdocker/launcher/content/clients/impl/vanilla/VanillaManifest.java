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

package io.mcdocker.launcher.content.clients.impl.vanilla;

import io.mcdocker.launcher.content.clients.ClientManifest;

public class VanillaManifest extends ClientManifest {

    private final String dataUrl;

    public VanillaManifest(String dataUrl, String name, int javaVersion, String mainClass, String startupArguments, String type) {
        super(name, javaVersion, mainClass, startupArguments, type);
        this.dataUrl = dataUrl;
    }

    public VanillaManifest(String dataUrl, String name, int javaVersion, String mainClass, String startupArguments) {
        this(dataUrl, name, javaVersion, mainClass, startupArguments, VanillaClient.class.getName());
    }

    public String getDataUrl() {
        return dataUrl;
    }

}
