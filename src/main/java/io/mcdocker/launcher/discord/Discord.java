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
import io.mcdocker.launcher.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Discord {

    private Core core;
    private boolean quit = false;

    public void init() throws IOException {
        File sdk = DiscordUtils.downloadDiscordSDK();
        if (sdk == null) {
            Logger.log("SDK is null. Continuing");
            return;
        }

        Core.init(sdk);
        CompletableFuture.runAsync(() -> {
            try(CreateParams params = new CreateParams()) {
                params.setClientID(889845849578962964L);
                params.setFlags(CreateParams.getDefaultFlags());

                try(Core core = new Core(params)) {
                    this.core = core;

                    Activity activity = new Activity();
                    activity.timestamps().setStart(Instant.now());
                    activity.setType(ActivityType.PLAYING);
                    activity.setDetails("Using MCDocker");
                    activity.assets().setLargeImage("logo_background");
                    activity.assets().setLargeText("Version " + MCDocker.version);

                    changeRPC(activity);

                    while(Config.getConfig().getConfigSerialized().general.DiscordRPC) {
                        core.runCallbacks();
                        try { Thread.sleep(16);}
                        catch(InterruptedException e) { e.printStackTrace(); }
                    }

                }
            }
        });

    }

    public void shutdown() {
        quit = true;
        core.close();
    }

    private void changeRPC(Activity activity) {
        core.activityManager().updateActivity(activity);
    }

}
