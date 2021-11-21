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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.content.clients.Client;
import io.mcdocker.launcher.content.clients.impl.vanilla.VanillaClient;
import io.mcdocker.launcher.content.clients.impl.vanilla.VanillaManifest;
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
import java.util.*;
import java.util.concurrent.CompletableFuture;

public class FabricClient extends Client<FabricManifest> {

    private final JsonObject data;
    private final File versionsFolder = new File(Folders.USER_DATA, "versions/" + "vanilla"); // Fabric uses the vanilla client and uses libraries to run alongside with it hence why there is no point in downloading to a new folder
    private final JsonObject vanillaManifestJson;

    public FabricClient(String dataUrl, JsonObject data, String loader, VanillaManifest vanillaClient) {
        super(new FabricManifest(
                dataUrl,
                data.get("inheritsFrom").getAsString(),
                16,
                data.get("mainClass").getAsString(),
                appendArguments(parseArguments(data.getAsJsonObject("arguments").getAsJsonArray("jvm")), parseArguments(data.getAsJsonObject("arguments").getAsJsonArray("game")), parseArgumentsFromVanillaManifest(vanillaClient)),
                data.get("inheritsFrom").getAsString(),
                loader,
                vanillaClient
        ));
        this.data = data;
        this.vanillaManifestJson = gson.fromJson(RequestBuilder.getBuilder()
                .setURL(vanillaClient.getDataUrl())
                .setMethod(Method.GET)
                .send(), JsonObject.class);
    }

    public FabricClient(FabricManifest manifest) {
        super(manifest);
        this.data = gson.fromJson(RequestBuilder.getBuilder()
                .setURL(manifest.getDataUrl())
                .setMethod(Method.GET)
                .send(), JsonObject.class);;
        this.vanillaManifestJson = gson.fromJson(RequestBuilder.getBuilder()
                .setURL(manifest.getVanillaManifest().getDataUrl())
                .setMethod(Method.GET)
                .send(), JsonObject.class);
    }

    public String getVersionName() {
        return String.format("%s-%s-%s", "fabric-loader", manifest.getLoader(), manifest.getInheritance());
    }

    private static String parseArgumentsFromVanillaManifest(VanillaManifest client) {
        String args = client.getStartupArguments();
        return args.split("\\$\\{main_class}")[1];
    }

    private static String parseArguments(JsonArray data) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isJsonObject()) continue;
            builder.append(data.get(i).getAsString());
            if (i != data.size() - 1) builder.append(" ");
        }
        return builder.toString();
    }

    private static String appendArguments(String jvm, String fabricGameArguments, String minecraftArguments) {

        System.out.println("-XX:-UseAdaptiveSizePolicy " +
                "-XX:-OmitStackTraceInFastThrow " +
                "-Dminecraft.launcher.brand=mc-docker " +
                "-Dminecraft.launcher.version=1 " +
                "-Djava.library.path=${natives} " +
                "-Xms${min_memory}M " +
                "-Xmx${max_memory}M " +
                "-cp ${libraries} " +
                jvm +
                "${main_class}" +
                fabricGameArguments +
                minecraftArguments);

        return "-XX:-UseAdaptiveSizePolicy " +
                "-XX:-OmitStackTraceInFastThrow " +
                "-Dminecraft.launcher.brand=mc-docker " +
                "-Dminecraft.launcher.version=1 " +
                "-Djava.library.path=${natives} " +
                "-Xms${min_memory}M " +
                "-Xmx${max_memory}M " +
                "-cp ${libraries} " +
                jvm +
                "${main_class}" +
                fabricGameArguments +
                minecraftArguments;
    }

    @Override
    public String getTypeName() {
        return "fabric";
    }

    @Override
    public CompletableFuture<File> downloadClient() {
        return CompletableFuture.supplyAsync(() -> {
            versionsFolder.mkdirs();

            File client = new File(versionsFolder, manifest.getInheritance() + ".jar");
            if (client.exists()) return client;

            String url = vanillaManifestJson.getAsJsonObject("downloads").getAsJsonObject("client").get("url").getAsString();
            HTTPUtils.download(url, client);

            return client;
        });
    }

    @Override
    public CompletableFuture<List<String>> downloadLibraries() {
        return CompletableFuture.supplyAsync(() -> {
            librariesFolder.mkdir();

            VanillaClient client = new VanillaClient(manifest.getVanillaManifest());
            List<String> librariesList = new ArrayList<>(client.downloadLibraries().join());

            for (JsonElement library : data.getAsJsonArray("libraries")) {
                JsonObject lib = library.getAsJsonObject();


                String name = lib.get("name").getAsString();
                String url = lib.get("url").getAsString();

                String libPackage = name.split(":")[0];
                String libName = name.split(":")[1];
                String libVersion = name.split(":")[2];

                String path = libPackage.replaceAll("\\.", "/") + "/" + libName + "/" + libVersion + "/" + libName + "-" + libVersion + ".jar";

                if (!librariesFolder.exists()) librariesFolder.mkdirs();
                File libraryFile = new File(librariesFolder, path);

                if (!libraryFile.getParentFile().exists()) libraryFile.getParentFile().mkdirs();
                if(!libraryFile.exists()) HTTPUtils.download(url + path, libraryFile);

                librariesList.add(libraryFile.getPath());
            }

//            librariesList.add("/home/drydandyfan/.mcdocker/versions/vanilla/1.17.1.jar");
            // TODO: Add base client to libraries list

            return librariesList;
        });
    }

    @Override
    protected void downloadNatives(JsonArray natives) {}

    @Override
    public CompletableFuture<String> downloadAssets() {
        VanillaClient client = new VanillaClient(manifest.getVanillaManifest());
        return client.downloadAssets();
    }

}
