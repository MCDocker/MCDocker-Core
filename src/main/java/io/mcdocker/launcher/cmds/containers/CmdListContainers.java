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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.container.Container;
import io.mcdocker.launcher.utils.Table;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Command(name = "list", description = "List all containers")
public class CmdListContainers implements Runnable {

    @Option(names = {"--json"}, description = "Output in JSON format")
    boolean json;

    @Override
    public void run() {
        List<Container> containers = Container.getContainers();

        if(json) {
            JsonArray array = new JsonArray();
            for(Container container : containers) {
                JsonObject obj = new JsonObject();
                obj.addProperty("folder", container.getFolder().getPath());
                obj.addProperty("id", container.getDockerfile().getId().toString());
                obj.addProperty("name", container.getDockerfile().getName());
                array.add(obj);
            }
            System.out.println(array);
            return;
        }

        Table table = new Table();

        List<List<String>> rows = new ArrayList<>();
        for (Container container : containers) {
            List<String> row = new ArrayList<>();
            row.add(container.getDockerfile().getName());
            row.add(container.getDockerfile().getClient().get("name").getAsString());
            row.add(String.valueOf(container.getDockerfile().getId()));

            rows.add(row);
        }

        table.setHeaders(Arrays.asList("Name", "Version", "ID"));
        table.setRows(rows);

        System.out.println(table);
        System.out.println("Total amount of containers: " + containers.size());

    }
}
