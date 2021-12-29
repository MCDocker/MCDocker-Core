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

@Command(name = "locate", description = "Locate a container")
public class CmdLocateContainer implements Runnable{

    @Parameters(index = "0", description = "The container ID")
    String id;

    @Override
    public void run() {
        Container container = Container.getContainerById(id);
        if(container == null){
            System.out.println("Container not found");
            return;
        }

        System.out.println(container.getDockerFile().getPath());

    }
}
