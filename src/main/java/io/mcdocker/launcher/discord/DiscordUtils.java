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

package io.mcdocker.launcher.discord;

import io.mcdocker.launcher.utils.OSUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Locale;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class DiscordUtils {

    public static File downloadDiscordSDK() throws IOException {
        String name = "discord_game_sdk";
        String suffix;


        String arch = System.getProperty("os.arch").toLowerCase(Locale.ROOT);

        switch (OSUtils.getOperatingSystem()) {
            case WINDOWS: suffix = ".dll"; break;
            case LINUX: suffix = ".so"; break;
            case MACOS: suffix = ".dylib"; break;
            default:
            case OTHER: throw new RuntimeException("Cannot determine OS Type");
        }

        if(arch.equals("amd64"))
            arch = "x86_64";

        File launcher_natives = new File(OSUtils.getUserData() + File.separator + "launcher_natives");
        launcher_natives.mkdir();

        File dir = new File(launcher_natives + File.separator + "discord");
        dir.mkdir();

        File discordSDK = new File(dir + File.separator + name + suffix);

        if(discordSDK.exists()) return discordSDK;

        String zipPath = "lib" + File.separator + arch + File.separator + name + suffix;

        URL downloadUrl = new URL("https://dl-game-sdk.discordapp.net/2.5.6/discord_game_sdk.zip");
        ZipInputStream zin = new ZipInputStream(downloadUrl.openStream());

        ZipEntry entry;
        while((entry = zin.getNextEntry()) != null) {
            if(entry.getName().equals(zipPath)) {

                Files.copy(zin, discordSDK.toPath());
                zin.close();

                return discordSDK;
            }
            zin.closeEntry();
        }
        zin.close();
        return null;
    }

}
