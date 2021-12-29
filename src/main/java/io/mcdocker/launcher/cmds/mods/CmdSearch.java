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

package io.mcdocker.launcher.cmds.mods;

import io.mcdocker.launcher.content.mods.ModDetails;
import io.mcdocker.launcher.content.mods.impl.curseforge.CurseForge;
import io.mcdocker.launcher.content.mods.impl.modrinth.Modrinth;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Command(name = "search", description = "Search for mods")
public class CmdSearch implements Runnable{

    @Option(names = {"-p", "--providers"}, description = "Providers to use (curseforge, modrinth)")
    List<String> providers;

    @Override
    public void run() {
        if(providers == null || providers.size() == 0) providers = Arrays.asList("curseforge", "modrinth");

        System.out.println("Searching for mods from " + providers.size() + " providers (" + String.join(", ", providers) + ")");

        List<ModDetails> mods = new ArrayList<>();

        if(providers.contains("modrinth")) {
            Modrinth modrinth = new Modrinth();
            mods.addAll(modrinth.getMods().join());
        }

        if (providers.contains("curseforge")) {
            CurseForge curseForge = new CurseForge();
            mods.addAll(curseForge.getMods().join());
        }

        System.out.println("Found " + mods.size() + " mods");
        System.out.println(mods.stream().map(ModDetails::getName).collect(Collectors.joining(", ")));

        System.out.println("Search complete");
    }
}
