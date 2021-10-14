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

import de.jcm.discordgamesdk.Core;
import de.jcm.discordgamesdk.CreateParams;
import de.jcm.discordgamesdk.activity.Activity;
import de.jcm.discordgamesdk.activity.ActivityType;
import io.mcdocker.launcher.MCDocker;
import io.mcdocker.launcher.config.Config;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.concurrent.CompletableFuture;

public class Discord {

    private Core core;
    private boolean running = true;

    public void init() {
        CompletableFuture.runAsync(() -> {
            try {
                File sdk = DiscordUtils.downloadDiscordSDK();
                if (sdk == null) return;

                Core.init(sdk);
                CreateParams params = new CreateParams();
                params.setClientID(889845849578962964L);
                params.setFlags(CreateParams.getDefaultFlags());

                Core core = new Core(params);
                this.core = core;

                Activity activity = new Activity();
                activity.timestamps().setStart(Instant.now());
                activity.setType(ActivityType.PLAYING);
                activity.setDetails("Using MCDocker");
                activity.assets().setLargeImage("logo_background");
                activity.assets().setLargeText("v" + MCDocker.version);

                changeRPC(activity);

                while (running) {
                    if (Config.getConfig().getConfigSerialized().general.DiscordRPC) core.runCallbacks();
                    try {
                        Thread.sleep(16);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

    }

    public void shutdown() {
        running = false;
        core.close();
    }

    private void changeRPC(Activity activity) {
        core.activityManager().updateActivity(activity);
    }

}
