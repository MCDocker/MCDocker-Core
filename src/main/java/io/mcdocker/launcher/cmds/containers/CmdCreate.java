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

package io.mcdocker.launcher.cmds.containers;

import io.mcdocker.launcher.container.Container;
import io.mcdocker.launcher.container.Dockerfile;
import io.mcdocker.launcher.content.clients.impl.fabric.Fabric;
import io.mcdocker.launcher.content.clients.impl.fabric.FabricManifest;
import io.mcdocker.launcher.content.clients.impl.vanilla.Vanilla;
import io.mcdocker.launcher.content.clients.impl.vanilla.VanillaManifest;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.IOException;

@Command(name = "create", description = "Creates a new container")
public class CmdCreate implements Runnable {

    @Option(names = {"-v", "--version"}, required = true, description = "The Minecraft version to use")
    private String version;

    @Option(names = {"-t", "--type"}, required = false, defaultValue = "vanilla", description = "The client type (forge, fabric, vanilla, optifine, custom)")
    private String clientType;

    @Option(names = {"-n", "--name"}, description = "The name of the container")
    private String name;

    @Override
    public void run() {
        try {
            Container container = new Container(new Dockerfile());

            // TODO: Improve this code lmfao

            switch (clientType.toLowerCase()) {
                default:
                    VanillaManifest vanillaClient = container.getDockerfile().getClient(VanillaManifest.class);
                    if (vanillaClient == null) {
                        vanillaClient = new Vanilla().getClient(version).join().get().getManifest();
                        container.getDockerfile().setClient(vanillaClient);
                    }
                    break;
                case "forge":
                    break;
                case "fabric":
                    FabricManifest fabricClient = container.getDockerfile().getClient(FabricManifest.class);
                    if (fabricClient == null) {
                        fabricClient = new Fabric().getClient(version).join().get().getManifest();
                        container.getDockerfile().setClient(fabricClient);
                    }
                    break;

                case "optifine":
                    break;

                case "custom":
                    break;
            }

            if(name != null) container.getDockerfile().setName(name);

            container.save();

            System.out.println("Created container with id '" + container.getDockerfile().getId() + "'");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
