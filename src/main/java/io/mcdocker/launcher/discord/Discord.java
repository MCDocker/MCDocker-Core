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
import io.mcdocker.launcher.content.clients.Client;
import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.Locale;
import java.util.concurrent.*;

public class Discord {

    private boolean running = true;

    private final Core core;

    public Discord(Core core) {
        this.core = core;
    }

    public static void init() {
        Logger.log("Initiating Discord");
        try {
            File sdk = DiscordUtils.downloadDiscordSDK();
            if (sdk == null) return;

            Core.init(sdk);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
//        setPresence(presenceInit());

        while (running) {
            core.runCallbacks();
            try {
                Thread.sleep(16);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setPresence(Activity activity) {
        core.activityManager().clearActivity();
        core.activityManager().updateActivity(activity);
    }

    public void shutdown() {
        running = false;
    }

    public static Activity presenceInit() {
        Activity activity = new Activity();
        activity.timestamps().setStart(Instant.now());
        activity.setType(ActivityType.PLAYING);
        activity.setDetails("Using MCDocker");
        activity.assets().setLargeImage("logo_background");
        activity.assets().setLargeText("v" + MCDocker.getVersion());

        return activity;
    }

    public static Activity presencePlaying(Client<?> client) {
        Activity activity = new Activity();
        activity.setDetails("Playing Minecraft");
        activity.setState("Version " + client.getManifest().getName());
        activity.setType(ActivityType.PLAYING);
        activity.timestamps().setStart(Instant.now());
        activity.assets().setLargeImage(getLargeImage(client));
        activity.assets().setLargeText(StringUtils.uppercaseFirstLetterOfString(client.getTypeName()));

        return activity;
    }

    private static String getLargeImage(Client<?> client) {
        switch (client.getTypeName().toLowerCase()) {
            case "forge": return "mc_forge";
            case "fabric": return "mc_fabric";
            default: return "mc_vanilla";
        }
    }

}
