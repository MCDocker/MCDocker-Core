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

package io.mcdocker.launcher.fx.components;

import io.mcdocker.launcher.MCDocker;
import io.mcdocker.launcher.auth.Account;
import io.mcdocker.launcher.auth.Authentication;
import io.mcdocker.launcher.auth.impl.OfflineAuth;
import io.mcdocker.launcher.container.Container;
import io.mcdocker.launcher.container.Dockerfile;
import io.mcdocker.launcher.content.clients.Client;
import io.mcdocker.launcher.content.clients.ClientManifest;
import io.mcdocker.launcher.content.clients.impl.vanilla.Vanilla;
import io.mcdocker.launcher.content.clients.impl.vanilla.VanillaManifest;
import io.mcdocker.launcher.content.mods.impl.curseforge.CurseForge;
import io.mcdocker.launcher.content.mods.impl.modrinth.Modrinth;
import io.mcdocker.launcher.discord.Discord;
import io.mcdocker.launcher.launch.LaunchWrapper;
import io.mcdocker.launcher.utils.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static io.mcdocker.launcher.utils.FXUtils.editFx;

public class PlayButton extends AnchorPane {

    public PlayButton() {
        FXMLLoader button = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/PlayButton.fxml"));
        button.setController(this);
        button.setRoot(this);

        try {
            Authentication auth = /*new MicrosoftAuth();*/ new OfflineAuth();
            CompletableFuture<Account> accountFuture = auth.authenticate(Logger::debug);

            List<Container> containers = Container.getContainers();
            if (containers.size() == 0) containers.add(new Container(new Dockerfile()));

            Container container = containers.get(0);
//            container.getDockerfile().setMods(List.of(
//                    new Modrinth().getMod("AANobbMI").join().getVersion("YAGZ1cCS").join().getManifest(),
//                    new CurseForge().getMod("238222").join().getVersion("2370313").join().getManifest()
//            ));
            VanillaManifest client = container.getDockerfile().getClient(VanillaManifest.class);
            if (client == null) {
                client = new Vanilla().getClient("1.17").join().get().getManifest();
                container.getDockerfile().setClient(client);
            }
            container.save();

            button.load();

            Button btn = (Button) lookup("#playButton");
            String playText = "PLAY " + client.getName();
            btn.setText(playText);
            ClientManifest finalClient = client;
            btn.setOnAction(actionEvent -> {

                play(finalClient, container, accountFuture, btn, playText);

            });
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void play(ClientManifest finalClient, Container container, CompletableFuture<Account> accountFuture, Button btn, String playText) {
        CompletableFuture.runAsync(() -> {
            try {
                Client<?> c = Client.of(finalClient);
                LaunchWrapper launchWrapper = new LaunchWrapper(container, c);
                Process process = launchWrapper.launch(accountFuture.get()).get();

                editFx(() -> {
                    btn.setDisable(true);
                    btn.setText("LAUNCHED");
                    MCDocker.getDiscord().setPresence(Discord.presencePlaying(c));
                });

                BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()));
                String line;
                while ((line = input.readLine()) != null) Logger.log(line);
                input.close();

                editFx(() -> {
                    btn.setDisable(false);
                    btn.setText(playText);
                    MCDocker.getDiscord().setPresence(Discord.presenceInit());
                });

                process.onExit().thenRun(() -> {
                    if(process.exitValue() != 0) {
                        new Popup("Game Crashed - " + process.exitValue(), "game crash error mesg");
                    }
                });

            } catch (IOException | InterruptedException | ExecutionException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

}
