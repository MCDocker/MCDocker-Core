package me.hottutorials.auth.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import me.hottutorials.auth.Account;
import me.hottutorials.auth.Authentication;
import me.hottutorials.auth.AuthenticationException;
import me.hottutorials.utils.StringUtils;
import me.hottutorials.utils.http.HTTPUtils;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MicrosoftAuth implements Authentication<MicrosoftAuth> {

    private final String clientID = "00000000402b5328";
    private final String oauthURL = "https://login.live.com/oauth20_authorize.srf?client_id=00000000402b5328&response_type=code&scope=service%3A%3Auser.auth.xboxlive.com%3A%3AMBI_SSL&redirect_uri=https%3A%2F%2Flogin.live.com%2Foauth20_desktop.srf";

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Override
    public CompletableFuture<Account> authenticate(Consumer<String> status) {
        CompletableFuture<Account> future = new CompletableFuture<>();

        WebView webView = new WebView();
        webView.getEngine().load(oauthURL);

        VBox vBox = new VBox(webView);
        Scene scene = new Scene(vBox, 450, 600);

        Stage window = new Stage();
        window.setTitle("Microsoft Authentication");
        window.setScene(scene);
        window.show();

        webView.getEngine().locationProperty().addListener((observableValue, s, after) -> {
            if (after.replaceAll("https://", "").replaceAll("http://", "").startsWith("login.live.com/oauth20_desktop.srf?code=")) {
                String code = HTTPUtils.getQuery(after).get("code");
                window.close();
                if (code == null) return;

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

                future.complete(account);
            }
        });
        return future;
    }

    private JsonObject codeToToken(String code) {
        String res = RequestBuilder.getBuilder()
                .setURL("https://login.live.com/oauth20_token.srf")
                .setBody(StringUtils.format("client_id=${0}&code=${1}&grant_type=authorization_code&redirect_uri=${2}", clientID, code, "https://login.live.com/oauth20_desktop.srf"))
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
