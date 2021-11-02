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

package io.mcdocker.launcher.auth.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.javalin.Javalin;
import io.mcdocker.launcher.auth.Account;
import io.mcdocker.launcher.auth.Authentication;
import io.mcdocker.launcher.auth.AuthenticationException;
import io.mcdocker.launcher.utils.StringUtils;
import io.mcdocker.launcher.utils.http.Method;
import io.mcdocker.launcher.utils.http.RequestBuilder;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import static io.mcdocker.launcher.MCDocker.getJavalin;

public class MicrosoftAuth implements Authentication {

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public CompletableFuture<Account> authenticate(Consumer<String> status) {
        CompletableFuture<Account> future = new CompletableFuture<>();

        String oauthURL = "https://login.live.com/oauth20_authorize.srf?client_id=3139b833-8d6f-4a6e-b406-5a075ee17fd4&response_type=code&scope=XboxLive.signin%20offline_access";

        getJavalin().get("/", (ctx) -> {
            if(ctx.queryParamMap().containsKey("code")) {
                String code = ctx.queryParam("code");

//                if (code == null) return;

                ctx.result("You may now close this window");

                // TODO DO AUTHENTICATION ON SERVER END

                /*
                status.accept("Converting Code to Token");

                JsonObject codeToToken = codeToToken(code);
                String accessToken = codeToToken.get("access_token").getAsString();

                status.accept("Authenticating with XBL");

                JsonObject authWithXBL = authWithXBL(accessToken);
                String xblToken = authWithXBL.get("Token").getAsString();
                String userhash = authWithXBL.getAsJsonObject("DisplayClaims").getAsJsonArray("xui").get(0).getAsJsonObject().get("uhs").getAsString();

                status.accept("Authenticating with XSTS");

                JsonObject authWithXSTS = authWithXSTS(xblToken);
                if (authWithXSTS.has("XErr")) {
                    switch (authWithXSTS.get("XErr").getAsString()) {
                        case "2148916233":
                            future.completeExceptionally(new MicrosoftAuthenticationException("You do not have an Xbox Account. Please make one."));
                        case "2148916235":
                            future.completeExceptionally(new MicrosoftAuthenticationException("Xbox Live is not available in your country."));
                        case "2148916238":
                            future.completeExceptionally(new MicrosoftAuthenticationException("Your account is marked as \"Under 18\". Please add this account to a family or choose another account."));
                    }
                    return;
                }
                String xstsToken = authWithXSTS.get("Token").getAsString();

                JsonObject authWithMC = authWithMC(userhash, xstsToken);
                String mcAccessToken = authWithMC.get("access_token").getAsString();

                JsonObject checkOwnership = checkOwnership(mcAccessToken);
                if (checkOwnership.getAsJsonArray("items").isEmpty()) {
                    future.completeExceptionally(new MicrosoftAuthenticationException("You do not own Minecraft. Please buy it at minecraft.net"));
                    return;
                }

                Account account = getAccount(mcAccessToken);
                if (account == null) {
                    future.completeExceptionally(new MicrosoftAuthenticationException("Minecraft Account could not be found."));
                    return;
                }

                status.accept("Welcome, " + account.getUsername() + ".");
                 */

                future.complete(new Account("player", "0", "0", new JsonArray()));
            }
        });

        Desktop desktop = Desktop.getDesktop();
        try {
            desktop.browse(URI.create(oauthURL));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return future;
    }

    private JsonObject codeToToken(String code) {
        String clientID = "3139b833-8d6f-4a6e-b406-5a075ee17fd4";
        String res = RequestBuilder.getBuilder()
                .setURL("https://login.live.com/oauth20_token.srf")
                .setBody(StringUtils.format("client_id=${0}&code=${1}&grant_type=authorization_code&redirect_uri=${2}", clientID, code, "http://localhost:5005/"))
                .setMethod(Method.POST)
                .addHeader("Content-Type", "application/x-www-form-urlencoded").send(true);

        return gson.fromJson(res, JsonObject.class);
    }

    private JsonObject authWithXBL(String accessToken) {
        JsonObject body = new JsonObject();

        JsonObject properties = new JsonObject();
        properties.addProperty("AuthMethod", "RPS");
        properties.addProperty("SiteName", "user.auth.xboxlive.com");
        properties.addProperty("RpsTicket", accessToken);

        body.add("Properties", properties);
        body.addProperty("RelyingParty", "http://auth.xboxlive.com");
        body.addProperty("TokenType", "JWT");

        String res = RequestBuilder.getBuilder()
                .setURL("https://user.auth.xboxlive.com/user/authenticate")
                .setBody(gson.toJson(body))
                .setMethod(Method.POST)
                .addHeader("Content-Type", "application/json").addHeader("Accept", "application/json")
                .send(true);

        return gson.fromJson(res, JsonObject.class);
    }

    private JsonObject authWithXSTS(String xblToken) {
        JsonObject body = new JsonObject();

        JsonObject properties = new JsonObject();
        properties.addProperty("SandboxId", "RETAIL");

        JsonArray userTokens = new JsonArray();
        userTokens.add(xblToken);
        properties.add("UserTokens", userTokens);

        body.add("Properties", properties);
        body.addProperty("RelyingParty", "rp://api.minecraftservices.com/");
        body.addProperty("TokenType", "JWT");

        String res = RequestBuilder.getBuilder()
                .setURL("https://xsts.auth.xboxlive.com/xsts/authorize")
                .setBody(gson.toJson(body))
                .setMethod(Method.POST)
                .addHeader("Content-Type", "application/json").addHeader("Accept", "application/json")
                .send(true);

        return gson.fromJson(res, JsonObject.class);
    }

    private JsonObject authWithMC(String userhash, String xstsToken) {
        String res = RequestBuilder.getBuilder()
                .setURL("https://api.minecraftservices.com/authentication/login_with_xbox")
                .setBody(StringUtils.format("{\"identityToken\": \"XBL3.0 x=${0};${1}\"}", userhash, xstsToken))
                .setMethod(Method.POST)
                .send(true);

        return gson.fromJson(res, JsonObject.class);
    }

    private JsonObject checkOwnership(String accessToken) {
        String res = RequestBuilder.getBuilder()
                .setURL("https://api.minecraftservices.com/entitlements/mcstore")
                .setMethod(Method.GET)
                .addHeader("Authorization", "Bearer " + accessToken)
                .send(true);

        return gson.fromJson(res, JsonObject.class);

    }

    private Account getAccount(String accessToken) {
        String res = RequestBuilder.getBuilder()
                .setURL("https://api.minecraftservices.com/minecraft/profile")
                .setMethod(Method.GET)
                .addHeader("Authorization", "Bearer " + accessToken)
                .send(true);

        JsonObject profile = gson.fromJson(res, JsonObject.class);

        if(profile.has("error")) return null;

        return new Account(profile.get("name").getAsString(), profile.get("id").getAsString(), accessToken, profile.get("skins").getAsJsonArray());
    }


    public static class MicrosoftAuthenticationException extends AuthenticationException {

        public MicrosoftAuthenticationException(String message) {
            super(message);
        }

    }

}
