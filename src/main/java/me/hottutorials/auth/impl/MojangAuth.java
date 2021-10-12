package me.hottutorials.auth.impl;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import me.hottutorials.auth.Account;
import me.hottutorials.auth.Authentication;
import me.hottutorials.auth.AuthenticationException;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

public class MojangAuth implements Authentication {
    private final String clientId;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static JsonObject credentials;

    public MojangAuth() {
        File file = new File(OSUtils.getMinecraftPath() + "\\clientId.txt"); //So we use the same clientToken as the launcher so we don't invalidate other accessTokens
        String clientId;
        try {
            if (!file.exists()) {
                file.mkdir();
                clientId = UUID.randomUUID().toString();
                FileWriter fileWriter = new FileWriter(file);
                fileWriter.write(clientId);
                fileWriter.close();
            } else {
                BufferedReader reader = new BufferedReader(new FileReader(file));
                clientId = reader.readLine();
                reader.close();
            }
        } catch (Exception ex) {
            clientId = UUID.randomUUID().toString();
        }
        this.clientId = clientId;
        credentials.addProperty("clientToken", this.clientId);
    }

    @Override
    public CompletableFuture<Account> authenticate(Consumer<String> status) {
        CompletableFuture<Account> future = new CompletableFuture<>();

        String email = "<email>";
        String password = "<password>";

        JsonObject creds = credentials.deepCopy();
        creds.addProperty("username", email);
        creds.addProperty("password", password);

        String res = RequestBuilder.getBuilder()
                .setURL("https://authserver.mojang.com/authenticate")
                .setBody(gson.toJson(creds))
                .setMethod(Method.POST)
                .addHeader("Content-Type", "application/json").addHeader("Accept", "application/json")
                .addHeader("User-Agent", "MCDocker")
                .send(false);
        if(res == null) {
            future.completeExceptionally(new MojangAuthenticationException("Invalid Login"));
            return future;
        }
        JsonObject reply = gson.fromJson(res, JsonObject.class);
        if (!reply.has("accessToken")) {
            future.completeExceptionally(new MojangAuthenticationException("You do not own Minecraft. Please buy it at minecraft.net"));
            return future;
        }
        String accessToken = reply.get("accessToken").getAsString();
        Account account = getAccount(accessToken);
        if (account == null) {
            future.completeExceptionally(new MojangAuthenticationException("Minecraft Account could not be found."));
            return future;
        }

        status.accept("Welcome, " + account.getUsername() + ".");

        future.complete(account);
        return future;
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
    static {
        credentials = new JsonObject();
        JsonObject agent = new JsonObject();
        agent.addProperty("name", "Minecraft");
        agent.addProperty("version", 1);
        credentials.add("agent", agent);
    }

    public static class MojangAuthenticationException extends AuthenticationException {

        public MojangAuthenticationException(String message) {
            super(message);
        }

    }


}
