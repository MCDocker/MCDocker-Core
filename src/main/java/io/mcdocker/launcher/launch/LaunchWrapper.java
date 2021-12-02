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

package io.mcdocker.launcher.launch;

import com.google.gson.GsonBuilder;
import io.mcdocker.launcher.MCDocker;
import io.mcdocker.launcher.auth.Account;
import io.mcdocker.launcher.container.Container;
import io.mcdocker.launcher.content.clients.Client;
import io.mcdocker.launcher.discord.Discord;
import io.mcdocker.launcher.utils.Folders;
import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.OperatingSystem;
import io.mcdocker.launcher.utils.StringUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class LaunchWrapper {

    private static final File versionsFolder = new File(Folders.USER_DATA, "versions");
    private final static File nativesFolder = new File(Folders.USER_DATA, "natives");
    private final static File assetsFolder = new File(Folders.USER_DATA, "assets");

    private final Container container;
    private final Client<?> client;

    public LaunchWrapper(Container container, Client<?> client) {
        this.container = container;
        this.client = client;
    }

    public CompletableFuture<Process> launch(Account account) {

        return CompletableFuture.supplyAsync(() -> {
            try {
                AtomicReference<String> javaPath = new AtomicReference<>("java");
                AtomicReference<String> index = new AtomicReference<>();
                List<String> librariesList = new ArrayList<>();

                long start = System.currentTimeMillis();

                CompletableFuture.allOf(
                        client.downloadJava().thenAccept(binFile -> {
                            if (binFile != null) javaPath.set(binFile.getPath() + File.separator + "java");
                            Logger.log("[x] Downloaded Java");
                        }),
                        client.downloadClient().thenRun(() -> Logger.log("[x] Downloaded Client")),
                        client.downloadLibraries().thenAccept(libraries -> {
                            librariesList.addAll(libraries);
                            Logger.log("[x] Downloaded Libraries");
                        }),
                        client.downloadAssets().thenAccept(i -> {
                            if (i != null) index.set(i);
                            Logger.log("[x] Downloaded Assets");
                        })
                ).join();

                long end = System.currentTimeMillis();
                Logger.log(StringUtils.format("Finished downloading in ${0}ms.", end - start));

                StringBuilder librariesBuilder = new StringBuilder();
                librariesList.forEach(s -> librariesBuilder.append(s.replace("\\", "/")).append((OperatingSystem.OS == OperatingSystem.WINDOWS ? ";" : ":")));
                if (librariesBuilder.length() == 0) throw new Exception("Libraries length is 0");
                librariesBuilder.deleteCharAt(librariesBuilder.toString().length() - 1);
                String libraries = librariesBuilder.toString();

                String arguments = client.getManifest().getStartupArguments()
                        .replace("${auth_player_name}", account.getUsername())
                        .replace("${version_name}", client.getManifest().getName())
                        .replace("${game_directory}", container.getFolder().getPath())
                        .replace("${assets_root}", assetsFolder.getPath())
                        .replace("${game_assets}", assetsFolder.getPath() + "/virtual/" + index.get())
                        .replace("${assets_index_name}", index.get())
                        .replace("${auth_uuid}", account.getUniqueId())
                        .replace("${auth_access_token}", account.getAccessToken())
                        .replace("${auth_session}", "0") // TODO: Figure out how to get this session ID.
                        .replace("${user_properties}", "{}") // TODO: Figure out these properties.
                        .replace("${user_type}", "1")
                        .replace("${natives}", nativesFolder + "/" + client.getManifest().getName() + "/")
                        .replace("${libraries}", libraries)
                        .replace("${min_memory}", "512")
                        .replace("${max_memory}", container.getDockerfile().getMemory().toString())
                        .replace("${main_class}", client.getManifest().getMainClass())
                        .replace("${version_type}", "MCDocker");

                MCDocker.initDiscord();
                if(MCDocker.getDiscord() != null) MCDocker.getDiscord().setPresence(Discord.presencePlaying(client));

                return Runtime.getRuntime().exec(javaPath.get() + " " + arguments, null, container.getFolder()); // Set as null in order to work for
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }



}
