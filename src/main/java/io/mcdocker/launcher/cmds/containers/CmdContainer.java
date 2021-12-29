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

package io.mcdocker.launcher.cmds.containers;

import picocli.CommandLine.Command;
import io.mcdocker.launcher.cmds.containers.*;

@Command(name = "containers", description = "Container management", mixinStandardHelpOptions = true, subcommands = {
        CmdCreate.class,
        CmdDelete.class,
        CmdListContainers.class,
        CmdLocateContainer.class,
        CmdStart.class,
        CmdContainerInfo.class
})
public class CmdContainer {
}
