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

package io.mcdocker.launcher.protocol;

import io.mcdocker.launcher.utils.http.HTTPUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class ProtocolHandler {

    public final String installPath;

    public ProtocolHandler(String installPath) {
        this.installPath = installPath;
    }

    public CompletableFuture<Boolean> registerWindows(Consumer<String> status) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

//        ProcessBuilder builder = new ProcessBuilder("cmd.exe", "/c", "java -jar " + installPath + "\\MCDocker.jar");

        return future;
    }

    public CompletableFuture<Boolean> registerLinux() {
        return CompletableFuture.supplyAsync(() -> {
            HTTPUtils.download("https://raw.githubusercontent.com/MCDocker/Assets/main/linux.sh", new File(installPath + "/linux.sh"));

            File script = new File(installPath + "/linux.sh");

            ProcessBuilder builder = new ProcessBuilder("/bin/sh", '"' + script.getAbsolutePath() + '"');
            System.out.println(builder.command());
            try {
                Process process = builder.start();
                System.out.println(process.info().command());

                BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }

                System.out.println("Exit code: " + process.onExit().join().exitValue());
                return true;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        });
    }

}
