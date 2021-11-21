/*
 *
 *   MCDocker, an open source Minecraft launcher.
 *   Copyright (C) 2021 MCDocker
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 */

package io.mcdocker.launcher.content.clients.impl.fabric;

import io.mcdocker.launcher.content.clients.ClientManifest;
import io.mcdocker.launcher.content.clients.impl.vanilla.VanillaClient;
import io.mcdocker.launcher.content.clients.impl.vanilla.VanillaManifest;

public class FabricManifest extends ClientManifest {

    private final String dataUrl;
    private final String inheritance;
    private final String loader;
    private final VanillaManifest vanillaClient;

    public FabricManifest(String dataUrl, String name, int javaVersion, String mainClass, String startupArguments, String type, String inheritance, String loader, VanillaManifest vanillaClient) {
        super(name, javaVersion, mainClass, startupArguments, type);
        this.dataUrl = dataUrl;
        this.inheritance = inheritance;
        this.loader = loader;
        this.vanillaClient = vanillaClient;
    }

    public FabricManifest(String dataUrl, String name, int javaVersion, String mainClass, String startupArguments, String inheritance, String loader, VanillaManifest vanillaClient) {
        this(dataUrl, name, javaVersion, mainClass, startupArguments, FabricClient.class.getName(), inheritance, loader, vanillaClient);
    }

    public String getDataUrl() {
        return dataUrl;
    }
    public String getInheritance() { return inheritance; }
    public String getLoader() { return loader; }
    public VanillaManifest getVanillaManifest() { return vanillaClient; }

}
