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

import io.mcdocker.launcher.auth.Account;
import io.mcdocker.launcher.auth.Authentication;
import io.mcdocker.launcher.auth.impl.OfflineAuth;
import io.mcdocker.launcher.container.Container;
import io.mcdocker.launcher.container.Dockerfile;
import io.mcdocker.launcher.content.ClientType;
import io.mcdocker.launcher.content.clients.impl.vanilla.Vanilla;
import io.mcdocker.launcher.content.mods.Mod;
import io.mcdocker.launcher.content.mods.impl.modrinth.Modrinth;
import io.mcdocker.launcher.launch.LaunchWrapper;
import io.mcdocker.launcher.utils.Logger;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static io.mcdocker.launcher.utils.FXUtils.editFx;

public class PlayButton extends AnchorPane {

    public PlayButton() {
        FXMLLoader button = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/PlayButton.fxml"));
        button.setController(this);
        button.setRoot(this);

        new Vanilla().getClients().join().forEach(client -> System.out.println(client.getName()));

        try {
            Authentication auth = /*new MicrosoftAuth();*/ new OfflineAuth();
            CompletableFuture<Account> accountFuture = auth.authenticate(Logger::debug);

            List<Container> containers = Container.getContainers();
            if (containers.size() == 0) containers.add(new Container(new Dockerfile()));

            Container container = containers.get(0);
            container.getDockerfile().setMods(new Modrinth().getMods().join().stream().map(Mod::getManifest).collect(Collectors.toList()));
            container.save();

            button.load();

            Button btn = (Button) lookup("#playButton");
            String version = container.getDockerfile().getVersion();
            String playText = "PLAY " + version;
            btn.setText(playText);
            btn.setOnAction(actionEvent -> new Vanilla().getClient(version).thenAcceptAsync(v -> {
                try {
                    if (v.isEmpty()) return;
                    LaunchWrapper launchWrapper = new LaunchWrapper(container, v.get(), ClientType.VANILLA);
                    Process process = launchWrapper.launch(accountFuture.get()).get();
                    editFx(() -> {
                        btn.setDisable(true);
                        btn.setText("LAUNCHED");
                    });
                    BufferedReader input = new BufferedReader(new InputStreamReader(process.getErrorStream()));
                    String line;
                    while ((line = input.readLine()) != null) Logger.debug(line);
                    input.close();
                    editFx(() -> {
                        btn.setDisable(false);
                        btn.setText(playText);
                    });
                } catch (IOException | InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
