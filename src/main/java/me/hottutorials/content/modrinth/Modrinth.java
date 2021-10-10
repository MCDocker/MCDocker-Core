package me.hottutorials.content.modrinth;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.hottutorials.content.ClientType;
import me.hottutorials.content.Mod;
import me.hottutorials.content.ModProvider;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Modrinth implements ModProvider<ModrinthFilter> {

    private static final Gson gson = new Gson();

    private final String URL = "https://api.modrinth.com";

    @Override
    public CompletableFuture<List<Mod>> getMods(ModrinthFilter filter) {
        return CompletableFuture.supplyAsync(() -> {
            JsonObject response = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL(URL + "/api/v1/mod" + (filter == null ? "" : "?" + filter.getQuery()))
                    .setMethod(Method.GET)
                    .send(true), JsonObject.class);
            List<Mod> mods = new ArrayList<>();
            for (JsonElement hit : response.get("hits").getAsJsonArray()) {
                JsonObject mod = hit.getAsJsonObject();
                mods.add(toMod(mod));
            }
            return mods;
        });
    }

    public Mod toMod(JsonObject mod) {
        return new Mod(
                mod.get("title").getAsString(),
                mod.get("description").getAsString(),
                ClientType.CUSTOM, // TODO: Make this not custom.
                "https://modrinth.com/mod/" + mod.get("mod_id").getAsString().replace("local-", ""),
                mod.get("author").getAsString(),
                mod.get("versions").getAsJsonArray().get(mod.get("versions").getAsJsonArray().size() - 1).getAsString(),
                mod.get("icon_url").getAsString()
        );
    }

}
