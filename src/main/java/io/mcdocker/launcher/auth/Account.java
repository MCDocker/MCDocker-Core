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

package io.mcdocker.launcher.auth;

import com.google.gson.JsonArray;

public class Account {

    private final String username;
    private final String uuid;
    private final String accessToken;
    private final JsonArray skins;

    public Account(String username, String uuid, String accessToken, JsonArray skins) {
        this.username = username;
        this.uuid = uuid;
        this.accessToken = accessToken;
        this.skins = skins;
    }

    public String getUsername() {
        return username;
    }

    public String getUniqueId() {
        return uuid;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public JsonArray getSkins() {
        return skins;
    }

}
