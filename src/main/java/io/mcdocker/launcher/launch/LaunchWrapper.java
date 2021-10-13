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

import io.mcdocker.launcher.auth.Account;
import io.mcdocker.launcher.container.Container;
import io.mcdocker.launcher.content.ClientType;
import io.mcdocker.launcher.content.Version;
import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.OSUtils;
import io.mcdocker.launcher.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class LaunchWrapper {

    private static final File versionsFolder = new File(OSUtils.getUserData() + "versions");
    private final static File nativesFolder = new File(OSUtils.getUserData() + "natives");
    private final static File assetsFolder = new File(OSUtils.getUserData() + "assets");

    private final Container container;
    private final ClientType type;
    private final Version version;

    public LaunchWrapper(Container container, Version version, ClientType type) throws IOException {
        this.container = container;
        this.type = type;
        this.version = version;
    }

    public CompletableFuture<Process> launch(Account account, String... arguments) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                AtomicReference<String> javaPath = new AtomicReference<>("java");
                AtomicReference<String> index = new AtomicReference<>();
                List<String> librariesList = new ArrayList<>();

                long start = System.currentTimeMillis();

                CompletableFuture.allOf(
                        version.downloadJava().thenAccept(binFile -> {
                            if (binFile != null) javaPath.set(binFile.getPath() + File.separator + "java");
                            Logger.log("[x] Downloaded Java");
                        }),
                        version.downloadClient(type).thenRun(() -> Logger.log("[x] Downloaded Client")),
                        version.downloadLibraries().thenAccept(libraries -> {
                            librariesList.addAll(libraries);
                            Logger.log("[x] Downloaded Libraries");
                        }),
                        version.downloadAssets().thenAccept(i -> {
                            if (i != null) index.set(i);
                            Logger.log("[x] Downloaded Assets");
                        })
                ).join();

                long end = System.currentTimeMillis();
                Logger.log(StringUtils.format("Finished downloading in ${0}ms.", end - start));

                List<String> args = new ArrayList<>();
                args.add("-XX:-UseAdaptiveSizePolicy");
                args.add("-XX:-OmitStackTraceInFastThrow");
                args.add("-Dfml.ignorePatchDiscrepancies=true");
                args.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
                args.add("-Dminecraft.launcher.brand=mc-docker");

                args.add("-Djava.library.path=" + nativesFolder + "/" + version + "/");
                args.add("-Dminecraft.client.jar=" + versionsFolder + "/" + type.name().toLowerCase() + "/" + version + ".jar");

                StringBuilder librariesBuilder = new StringBuilder();
                librariesList.forEach(s -> librariesBuilder.append(s.replace("\\", "/")).append((OSUtils.isWindows() ? ";" : ":")));
                if (librariesBuilder.length() == 0) throw new Exception("Libraries length is 0");
                librariesBuilder.deleteCharAt(librariesBuilder.toString().length() - 1);
                args.add("-cp " + librariesBuilder + (OSUtils.isWindows() ? ";" : ":") + versionsFolder.getPath().replace("\\", "/") + "/" + type.name().toLowerCase() + "/" + version + ".jar");

                // TODO: Parse args from manifest.
                args.add("-Xmx3G");

                // Main class argument
                args.add(version.getManifest().get("mainClass").getAsString());

                args.add("--username " + account.getUsername());
                args.add("--uuid " + account.getUniqueId());
                args.add("--version " + version.getName());
                args.add("--accessToken " + account.getAccessToken());
                args.add("--userProperties {}");
                args.add("--assetsDir " + assetsFolder.getPath() + (index.get().equals("legacy") || index.get().equals("pre-1.6") ? "\\virtual\\" + index.get() : ""));
                args.add("--assetIndex " + index.get());

                StringBuilder argsBuilder = new StringBuilder();
                args.forEach(arg -> argsBuilder.append(arg).append(" "));
                args.addAll(Arrays.asList(arguments));


                System.out.println(argsBuilder);
                return Runtime.getRuntime().exec(javaPath.get() + " " + argsBuilder, new String[0], container.getFolder());
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }



}
