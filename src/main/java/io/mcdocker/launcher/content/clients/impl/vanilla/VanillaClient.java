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

package io.mcdocker.launcher.content.clients.impl.vanilla;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.clients.Client;
import io.mcdocker.launcher.utils.Folders;
import io.mcdocker.launcher.utils.NumberUtils;
import io.mcdocker.launcher.utils.OperatingSystem;
import io.mcdocker.launcher.utils.http.HTTPUtils;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public class VanillaClient extends Client<VanillaManifest> {

    private final JsonObject data;
    private final File versionsFolder = new File(Folders.USER_DATA, "versions/" + getTypeName());

    public VanillaClient(String dataUrl, JsonObject data) {
        super(new VanillaManifest(
                dataUrl,
                data.get("id").getAsString(),
                data.has("javaVersion") ? data.get("javaVersion").getAsJsonObject().get("majorVersion").getAsInt() : 8,
                data.get("mainClass").getAsString(),
                appendArguments(parseArguments(data))
        ));
        this.data = data;
    }

    public VanillaClient(VanillaManifest manifest) {
        super(manifest);
        this.data = gson.fromJson(RequestBuilder.getBuilder()
                .setURL(manifest.getDataUrl())
                .setMethod(Method.GET)
                .send(), JsonObject.class);
    }

    private static String parseArguments(JsonObject data) {
        if (data.has("arguments")) { // MODERN
            JsonArray argumentsArray = data.get("arguments").getAsJsonObject().get("game").getAsJsonArray();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < argumentsArray.size(); i++) {
                if (argumentsArray.get(i).isJsonObject()) continue;
                builder.append(argumentsArray.get(i).getAsString());
                if (i != argumentsArray.size() - 1) builder.append(" ");
            }
            return builder.toString();
        } else if (data.has("minecraftArguments")) { // LEGACY
            return data.get("minecraftArguments").getAsString();
        } return "--username ${auth_player_name} " + // FALLBACK LEGACY
                "--version ${version_name} " +
                "--gameDir ${game_directory} " +
                "--assetsDir ${assets_root} " +
                "--assetIndex ${assets_index_name} " +
                "--uuid ${auth_uuid} " +
                "--accessToken ${auth_access_token} " +
                "--userProperties ${user_properties} " +
                "--userType ${user_type}";
    }

    public static String appendArguments(String arguments) {
        return "-XX:-UseAdaptiveSizePolicy " +
                "-XX:-OmitStackTraceInFastThrow " +
                "-Dminecraft.launcher.brand=mc-docker " +
                "-Dminecraft.launcher.version=1 " +
                "-Djava.library.path=${natives} " +
                "-Xms${min_memory}M " +
                "-Xmx${max_memory}M " +
                "-cp ${libraries} " +
                "${main_class} "
                + arguments;
    }

    @Override
    public String getTypeName() {
        return "vanilla";
    }

    @Override
    public CompletableFuture<File> downloadClient() {
        return CompletableFuture.supplyAsync(() -> {
            versionsFolder.mkdirs();

            File client = new File(versionsFolder, manifest.getName() + ".jar");
            if (client.exists()) return client;

            String url = data.getAsJsonObject("downloads").getAsJsonObject("client").get("url").getAsString();

            HTTPUtils.download(url, client);

            return client;
        });
    }

    @Override
    public CompletableFuture<List<String>> downloadLibraries() {
        return CompletableFuture.supplyAsync(() -> {
            librariesFolder.mkdir();

            JsonArray natives = new JsonArray();
            List<String> librariesList = new ArrayList<>();
            Map<String, String> librariesMap = new HashMap<>();

            for (JsonElement library : data.getAsJsonArray("libraries")) {
                JsonObject lib = library.getAsJsonObject();
                if (lib.has("natives") || lib.has("extract")) {
                    natives.add(lib);
                    continue;
                }

                JsonObject artifact = lib.getAsJsonObject("downloads").getAsJsonObject("artifact");
                String path = artifact.get("path").getAsString();
                String url = artifact.get("url").getAsString();
                String name = lib.get("name").getAsString();
                String libraryName = name.split(":")[name.split(":").length - 2];
                String version = name.split(":")[name.split(":").length - 1];

                if(librariesMap.containsKey(libraryName) && NumberUtils.parseInt(librariesMap.get(libraryName).replaceAll("\\.", "")) != null && NumberUtils.parseInt(version.replaceAll("\\.", "")) != null && NumberUtils.parseInt(librariesMap.get(libraryName).replaceAll("\\.", "")) <= NumberUtils.parseInt(version.replaceAll("\\.", ""))) {
                    String pathToRemove = librariesFolder.getPath() + "/" + path.replace(version, librariesMap.get(libraryName)); // For some reason it errors when this is inline in the `librariesList.remove(pathToRemove)` part. I have no idea why
                    librariesMap.remove(libraryName);
                    librariesList.remove(pathToRemove);
                }

                librariesMap.put(libraryName, version);

                if (!librariesFolder.exists()) librariesFolder.mkdirs();
                File libraryFile = new File(librariesFolder, path);

                if (!libraryFile.getParentFile().exists()) libraryFile.getParentFile().mkdirs();
                if(!libraryFile.exists()) HTTPUtils.download(url, libraryFile);

                librariesList.add(libraryFile.getPath());
            }

            downloadNatives(natives);
            return librariesList;
        });
    }

    @Override
    protected void downloadNatives(JsonArray natives) {
        nativesFolder.mkdir();
        File currentNativeFolder = new File(nativesFolder, manifest.getName());
        if (currentNativeFolder.exists()) return;
        currentNativeFolder.mkdir();

        for (JsonElement nativ : natives) {
            JsonObject n = nativ.getAsJsonObject();
            boolean extract = n.has("extract") || n.has("natives");

            if (extract) {
                JsonObject classifiers = n.getAsJsonObject("downloads").getAsJsonObject("classifiers");

                JsonObject nativePlatform = null;

                switch (OperatingSystem.OS) {
                    case WINDOWS:
                        if (classifiers.has("natives-windows"))
                            nativePlatform = classifiers.getAsJsonObject("natives-windows");
                        else
                            nativePlatform = classifiers.getAsJsonObject("natives-windows-" + (System.getenv("ProgramFiles(x86)") != null ? "64" : "32"));
                        break;
                    case LINUX:
                        nativePlatform = classifiers.getAsJsonObject("natives-linux");
                        break;
                    case MACOS:
                        nativePlatform = classifiers.getAsJsonObject("natives-osx");
                        break;
                    case OTHER:
                        System.exit(1);
                        break;
                }

                if (nativePlatform == null) continue;

                String path = nativePlatform.get("path").getAsString();
                String url = nativePlatform.get("url").getAsString();

                String outputFileName = path.split("/")[path.split("/").length - 1];

                File nativeFile = new File(currentNativeFolder, outputFileName);
                if (nativeFile.exists()) continue;

                HTTPUtils.download(url, nativeFile);
                Archiver archiver = ArchiverFactory.createArchiver("jar");
                try {
                    archiver.extract(nativeFile, currentNativeFolder);
                    nativeFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public CompletableFuture<String> downloadAssets() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!assetsFolder.exists()) assetsFolder.mkdirs();
                File objectsFolder = new File(assetsFolder, "objects");
                if (!objectsFolder.exists()) objectsFolder.mkdirs();
                File indexesFolder = new File(assetsFolder, "indexes");
                if (!indexesFolder.exists()) indexesFolder.mkdirs();
                String indexId = data.get("assetIndex").getAsJsonObject().get("id").getAsString();
                File index = new File(indexesFolder, indexId + ".json");
                if (!index.exists()) HTTPUtils.download(data.get("assetIndex").getAsJsonObject().get("url").getAsString(), index);
                JsonObject objects = gson.fromJson(new FileReader(index), JsonObject.class).get("objects").getAsJsonObject();

                List<CompletableFuture<?>> futures = new ArrayList<>();
                objects.keySet().forEach(asset -> futures.add(CompletableFuture.runAsync(() -> {
                    try {
                        String hash = objects.get(asset).getAsJsonObject().get("hash").getAsString();
                        String shortHash = hash.substring(0, 2);
                        File hashFolder = new File(objectsFolder, shortHash);
                        if (!hashFolder.exists()) hashFolder.mkdirs();
                        File object = new File(hashFolder, hash);
                        if (!object.exists()) HTTPUtils.download("https://resources.download.minecraft.net/" + shortHash + "/" + hash, object);
                        if (indexId.equals("legacy") || indexId.equals("pre-1.6")) {
                            File virtualFolder = new File(assetsFolder, "virtual/" + indexId);
                            if (!virtualFolder.exists()) virtualFolder.mkdirs();
                            File mappedFile = new File(virtualFolder, asset);
                            if (!mappedFile.getParentFile().exists()) mappedFile.getParentFile().mkdirs();
                            if (!mappedFile.exists()) Files.copy(object.toPath(), mappedFile.toPath());
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })));

                CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[]{})).join();
                return indexId;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}
