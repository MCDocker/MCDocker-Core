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

package io.mcdocker.launcher.auth;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import io.mcdocker.launcher.utils.Folders;
import io.mcdocker.launcher.utils.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AccountsManager {

    private static AccountsManager instance;
    public static AccountsManager getInstance() {
        if (instance == null) instance = new AccountsManager();
        return instance;
    }

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    private final File accountsFile = new File(Folders.USER_DATA, "accounts.json");
    public File getAccountsFile() {return accountsFile;}

    public Account getCurrentAccount() {
        String uuid = getContent().get("current").getAsString();
        for(Account account : getAccounts()) {
            if(account.getUniqueId().equals(uuid)) return account;
        }
        return null;
    }

    public List<Account> getAccounts() {
        if(!accountsFile.exists()) init();
        List<Account> accounts = new ArrayList<>();
        getContent().getAsJsonArray("accounts").forEach(account -> accounts.add(new Account(
                account.getAsJsonObject().get("username").getAsString(),
                account.getAsJsonObject().get("uuid").getAsString(),
                account.getAsJsonObject().get("accessToken").getAsString(),
                account.getAsJsonObject().getAsJsonArray("skins")
        )));
        return accounts;
    }

    public JsonArray getAccountsAsJsonArray() {
        if(!accountsFile.exists()) init();
        return getContent().getAsJsonArray("accounts");
    }

    public JsonObject getContent() {
        if(!accountsFile.exists()) init();
        try {
            return gson.fromJson(new JsonReader(new FileReader(accountsFile)), JsonObject.class);
        } catch (Exception e) {
            Logger.err(e.getMessage());
            return null;
        }
    }

    public void addAccount(Account account) {
        JsonObject content = getContent();
        JsonArray accounts = getAccountsAsJsonArray();

        removeDuplicateAccount(account);

        JsonObject accountJson = new JsonObject();
        accountJson.addProperty("username", account.getUsername());
        accountJson.addProperty("uuid", account.getUniqueId());
        accountJson.addProperty("accessToken", account.getAccessToken());
        accountJson.add("skins", new JsonArray());

        accounts.add(accountJson);
        content.add("accounts", accounts);
        content.addProperty("current", account.getUniqueId());

        save(content);
    }

    private void removeDuplicateAccount(Account account) {
        JsonObject content = getContent();
        JsonArray accounts = getAccountsAsJsonArray();

        for(JsonElement element : getAccountsAsJsonArray())
            if(element.getAsJsonObject().has("uuid") && Objects.equals(element.getAsJsonObject().get("uuid").getAsString(), account.getUniqueId()))
                accounts.remove(element);

        content.add("accounts", accounts);
        save(content);
    }

    public void removeAccount(Account account) {
        JsonObject content = getContent();
        JsonArray accounts = getAccountsAsJsonArray();

        for(JsonElement element : getAccountsAsJsonArray())
            if(Objects.equals(gson.toJson(element.getAsJsonObject()), gson.toJson(account)))
                accounts.remove(element);

        content.add("accounts", accounts);
        save(content);
    }

    public void init() {
        try {
            if (!accountsFile.exists()) {
                accountsFile.createNewFile();
                JsonObject body = new JsonObject();

                body.addProperty("current", "0"); // UUID
                body.add("accounts", new JsonArray());

                save(body);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void save(JsonObject content) {
        try {
            FileWriter writer = new FileWriter(accountsFile);
            gson.toJson(content, writer);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
