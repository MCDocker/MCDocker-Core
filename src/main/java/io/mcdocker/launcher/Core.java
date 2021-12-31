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

package io.mcdocker.launcher;

import io.mcdocker.launcher.auth.Account;
import io.mcdocker.launcher.auth.AccountsManager;
import io.mcdocker.launcher.auth.impl.MicrosoftAuth;
import io.mcdocker.launcher.auth.impl.MojangAuth;
import io.mcdocker.launcher.cmds.accounts.CmdAccounts;
import io.mcdocker.launcher.cmds.containers.CmdContainer;
import io.mcdocker.launcher.cmds.mods.CmdMods;
import io.mcdocker.launcher.config.Config;
import io.mcdocker.launcher.plugins.PluginManager;
import io.mcdocker.launcher.utils.Folders;
import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.OperatingSystem;
import picocli.CommandLine;
import picocli.CommandLine.Command;

import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.CompletableFuture;

@Command(
        description = "A command line interface for managing MCDocker containers",
        mixinStandardHelpOptions = true,
        name = "mcdocker"
)
public class Core implements Runnable {

    private static final CommandLine cli = new CommandLine(new Core());

    public static void main(String[] args) throws IOException {
        boolean firstTime = !Config.getConfig().getConfigFile().exists();

        Folders.USER_DATA.mkdirs();
        Config.getConfig().init();
        AccountsManager.getInstance().init();
//        PluginManager.getManager().registerPlugins(); do this

        if(firstTime) {
            firstTime();
            return;
        }

        cli.addSubcommand(new CmdContainer());
        cli.addSubcommand(new CmdAccounts());
        cli.addSubcommand(new CmdMods());

        int exitCode = cli.execute(args);
        System.exit(exitCode);
    }

    private static void firstTime() {
        System.out.println("Welcome to MCDocker new user!");
        System.out.println("This is an interactive setup made to simplify your experience with MCDocker.");
        System.out.println("Lets begin!");
        System.err.println("\nIt is recommended to run with " + (OperatingSystem.OS == OperatingSystem.WINDOWS ? "administrator" : "sudo") + " in order to install everything. You can go back to this setup by doing 'mcdocker setup'!\n");

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.print("Login with Mojang (1) or Microsoft (2)? ");
            String authenticationMethod = scanner.nextLine();

            if(authenticationMethod.equals("1")) {
                System.out.println("Logging in using Mojang");

                System.out.print("Username: ");
                String username = scanner.nextLine();
                System.out.print("Password: ");
                String password = scanner.nextLine();

                MojangAuth auth = new MojangAuth();
                CompletableFuture<Account> accountCompletableFuture = auth.authenticate(username, password);

                CompletableFuture<Account> future = accountCompletableFuture.whenComplete((account, throwable) -> {
                    System.out.println("Logged in as " + account.getUsername());
                    AccountsManager.getInstance().addAccount(account);
                });

                future.join();
                break;
            }
            else if(authenticationMethod.equals("2")) {

                System.out.println("Logging in using Microsoft");

                MCDocker.startServer();
                MicrosoftAuth auth = new MicrosoftAuth();

                CompletableFuture<Account> accountCompletableFuture = auth.authenticate();

                CompletableFuture<Account> future = accountCompletableFuture.whenComplete((account, throwable) -> {
                    System.out.println("Logged in as " + account.getUsername());
                    AccountsManager.getInstance().addAccount(account);
                }).exceptionally(throwable -> {
                    MCDocker.stopServer();
                    return null;
                });

                if(future.join() == null) {
                    System.out.println("Failed to login. Is the MCDocker API online?");
                    break;
                }

                future.join();
                break;
            }

            System.out.println("Invalid authentication method. Please try again");
        }

        System.out.println("Setup complete!");
        System.exit(0);
    }

    @Command(name = "version", description = "Prints the MCDocker version")
    public void version() {
        System.out.println(MCDocker.getVersion());
    }

    @Override
    public void run() {
        Logger.logfile(cli.getUsageMessage());
    }

    public static CommandLine getCli() {return cli;}
}
