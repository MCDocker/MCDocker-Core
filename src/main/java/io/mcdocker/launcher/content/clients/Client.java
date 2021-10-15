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

package io.mcdocker.launcher.content.clients;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.utils.Folders;
import io.mcdocker.launcher.utils.OperatingSystem;
import io.mcdocker.launcher.utils.http.HTTPUtils;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

public abstract class Client<T extends ClientManifest> {

    protected static final Gson gson = new Gson();
    protected static final File javaFolder = new File(Folders.USER_DATA, "java");
    protected static final File librariesFolder = new File(Folders.USER_DATA, "libraries");
    protected static final File nativesFolder = new File(Folders.USER_DATA, "natives");
    protected static final File assetsFolder = new File(Folders.USER_DATA, "assets");

    protected final T manifest;

    public Client(T manifest) {
        this.manifest = manifest;
    }

    public T getManifest() {
        return manifest;
    }

    public abstract String getTypeName();

    public CompletableFuture<File> downloadJava() {
        if (!javaFolder.exists()) javaFolder.mkdirs();
        File javaVersionFolder = new File(javaFolder, manifest.getJavaVersion() + "");
        if (javaVersionFolder.exists())
            return CompletableFuture.completedFuture(new File(javaVersionFolder.listFiles()[0], "bin"));

        return CompletableFuture.supplyAsync(() -> {
            String tempOs = "linux";
            switch (OperatingSystem.OS) {
                case MACOS:
                    tempOs = "mac";
                case WINDOWS:
                    tempOs = "windows";
            }
            final String os = tempOs;
            String tempArch = "x64";
            switch (System.getProperty("os.arch")) { // TODO: Add more architectures.
                case "amd64":
                    tempArch = "x64";
            }
            final String arch = tempArch;
            JsonArray response = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL("https://api.adoptium.net/v3/assets/latest/" + manifest.getJavaVersion() + "/hotspot")
                    .setMethod(Method.GET).send(true), JsonArray.class);
            JsonObject binary = StreamSupport.stream(response.spliterator(), false).map(bin -> bin.getAsJsonObject().get("binary").getAsJsonObject())
                    .filter(bin -> (bin.get("image_type").getAsString().equals("jre") || bin.get("image_type").getAsString().equals("jdk")) && bin.get("os").getAsString().equalsIgnoreCase(os) && bin.get("architecture").getAsString().equalsIgnoreCase(arch))
                    .min((bin, bin2) -> {
                        if (bin.get("image_type").getAsString().equals("jre")) {
                            if (bin2.get("image_type").getAsString().equals("jre")) return 0;
                            else return -1;
                        } else return 1;
                    }).orElse(null);
            if (binary == null) return null;
            JsonObject pack = binary.get("package").getAsJsonObject();
            String fileName = pack.get("name").getAsString();
            javaVersionFolder.mkdirs();
            File archiveFile = new File(javaVersionFolder, fileName);
            HTTPUtils.download(pack.get("link").getAsString(), archiveFile);

            Archiver archiver = fileName.endsWith(".zip") ? ArchiverFactory.createArchiver("zip") : ArchiverFactory.createArchiver("tar", "gz");
            try {
                archiver.extract(archiveFile, javaVersionFolder);
                archiveFile.delete();
                return new File(javaVersionFolder.listFiles()[0], "bin");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }
    public abstract CompletableFuture<File> downloadClient();
    public abstract CompletableFuture<List<String>> downloadLibraries();
    protected abstract void downloadNatives(JsonArray natives);
    public abstract CompletableFuture<String> downloadAssets();

    public static Client<?> of(ClientManifest manifest) throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        for (Constructor<?> constructor : Class.forName(manifest.getType()).getDeclaredConstructors()) {
            Class<?> parameterType = constructor.getParameters()[0].getType();
            if (manifest.getClass().isAssignableFrom(parameterType)) {
                return (Client<?>) constructor.newInstance(parameterType.cast(manifest));
            }
        }
        return null;
    }

}
