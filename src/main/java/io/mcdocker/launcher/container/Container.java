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

package io.mcdocker.launcher.container;

import io.mcdocker.launcher.save.Save;
import io.mcdocker.launcher.utils.OSUtils;
import me.grison.jtoml.impl.Toml;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Container {

    private String name;
    private final Save save;

    private final Toml toml = new Toml();

    public Container(String name, Save save) {
        this.name = name;
        this.save = save;
    }

    private void createFolders() {
        final File containersPath = new File(OSUtils.getUserData() + "containers");
        if(!containersPath.exists()) containersPath.mkdir();

        final File containerFolder = new File(containersPath + "/" + name);
        if(!containerFolder.exists()) containerFolder.mkdir();
    }

    public File getDockerFile() {
        return new File(OSUtils.getUserData() + "containers" + "/" + name + "/.mcdocker");
    }

    public void createContainer() throws IOException {
        createFolders();

        if(!getDockerFile().exists()) getDockerFile().createNewFile();

        FileWriter writer = new FileWriter(getDockerFile());
        writer.write(Toml.serialize("meta", save));
        writer.flush();
        writer.close();
    }

    public Save getDockerContent() {
        System.out.println(toml.parseFile(getDockerFile()).get("meta"));
        Save format = toml.parseFile(getDockerFile()).getAs("meta.type", Save.class);
        return format;
    }



}
