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

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import io.mcdocker.launcher.utils.Folders;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Container {

    private static final Gson gson = new Gson();
    private static final File containersFolder = new File(Folders.USER_DATA, "containers");
    private static final String dockerfileName = "mcdocker.json";

    private final File folder;
    private final Dockerfile dockerfile;
    private final File dockerFile;

    public Container(Dockerfile dockerfile) throws IOException {
        this.dockerfile = dockerfile;
        this.folder = new File(containersFolder, dockerfile.getId().toString());
        this.dockerFile = new File(folder, dockerfileName);
        if (!folder.exists()) {
            folder.mkdirs();
            save();
        }
    }

    public Dockerfile getDockerfile() {
        return dockerfile;
    }

    public File getDockerFile() {
        return dockerFile;
    }

    public File getFolder() {
        return folder;
    }

    public void save() throws IOException {
        FileWriter writer = new FileWriter(dockerFile);
        gson.toJson(dockerfile, writer);
        writer.flush();
        writer.close();
    }

    public void delete() {
        Folders.deleteFolder(folder);
    }

    public static @Nullable Container fromFolder(File folder) throws IOException {
        File dockerFile = new File(folder, dockerfileName);
        if (dockerFile.exists()) return fromFile(dockerFile);
        else return null;
    }

    public static @NotNull Container fromFile(File dockerFile) throws IOException {
        return fromDockerfile(gson.fromJson(new FileReader(dockerFile), Dockerfile.class));
    }

    public static @NotNull Container fromDockerfile(Dockerfile dockerfile) throws IOException {
        return new Container(dockerfile);
    }

    public static Container getContainerById(String id) {
        for (Container container : getContainers()) {
            if (Objects.equals(container.getDockerfile().getId().toString(), id)) return container;
        }
        return null;
    }

    public static @NotNull List<Container> getContainers() {
        List<Container> containers = new ArrayList<>();
        if (containersFolder.exists()) {
            for (File folder : containersFolder.listFiles()) {
                if (!folder.isFile()) {
                    try {
                        Container container = fromFolder(folder);
                        if (container != null) containers.add(container);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else containersFolder.mkdirs();
        return containers;
    }

}
