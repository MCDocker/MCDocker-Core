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
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

@Command(name = "delete", description = "Delete a container")
public class CmdDelete implements Runnable {

    @Parameters(index = "0", description = "Container id")
    private String id;

    @Override
    public void run() {
        Container container = Container.getContainerById(id);

        if(container == null) {
            System.out.println("Could not find container '" + id + "'");
            return;
        }

        String name = container.getDockerfile().getName();
        container.delete();
        System.out.println("Successfully deleted container '" + id + "' with the name '" + name + "'");

    }

}
