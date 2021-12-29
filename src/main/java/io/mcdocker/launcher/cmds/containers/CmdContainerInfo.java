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

import com.google.gson.Gson;
import io.mcdocker.launcher.container.Container;
import io.mcdocker.launcher.utils.Table;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.util.Arrays;
import java.util.List;

@Command(name = "info", description = "Get information about a container")
public class CmdContainerInfo implements Runnable{

    @Parameters(index = "0", description = "Container id")
    private String id;

    @Option(names = {"--json"}, description = "Output in JSON format")
    private boolean json;

    @Override
    public void run() {
        Container container = Container.getContainerById(id);

        if(container == null) {
            System.out.println("Could not find container '" + id + "'");
            return;
        }

        if(json) {
            System.out.println(new Gson().toJson(container));
            return;
        }


        Table table = new Table();
        table.setHeaders(Arrays.asList("Name", "Version", "ID"));
        table.setRows(List.of(Arrays.asList(
                container.getDockerfile().getName(),
                container.getDockerfile().getClient().get("name").getAsString(),
                String.valueOf(container.getDockerfile().getId()),
                container.getDockerFile().getParentFile().getPath()
        )));

        System.out.println(table);

    }

}
