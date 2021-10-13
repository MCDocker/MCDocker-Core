package io.mcdocker.launcher.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import io.mcdocker.launcher.utils.Logger;
import io.mcdocker.launcher.utils.OSUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Config {

    private static final Config INSTANCE = new Config();
    public static Config getConfig() {return INSTANCE;}

    private final File configFile = new File(OSUtils.getUserData() + File.separator + "config.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void init() throws IOException {
        Logger.log("Initiating Config");
        if(!configFile.exists()) {
            configFile.createNewFile();
            save(new ConfigSerializer());
            Logger.log("Created new config");
        }
        Logger.log("Loading config from '" + configFile.getPath() + "'");
    }

    public void save(ConfigSerializer cfg) {
        try {
            FileWriter writer = new FileWriter(configFile);

            writer.write(gson.toJson(cfg));
            writer.flush();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ConfigSerializer getConfigSerialized() {
        try {
            JsonReader reader = new JsonReader(new FileReader(configFile));
            return gson.fromJson(reader, ConfigSerializer.class);
        } catch (FileNotFoundException e) {
            Logger.err("Config file not found");
            save(new ConfigSerializer());
            return new ConfigSerializer();
        }
    }

    public JsonObject getConfigJson() {
        try {
            JsonReader reader = new JsonReader(new FileReader(configFile));
            return gson.fromJson(reader, JsonObject.class);
        } catch (FileNotFoundException e) {
            Logger.err("Config file not found");
            save(new ConfigSerializer());
            return gson.fromJson(gson.toJson(new ConfigSerializer()), JsonObject.class);
        }
    }

    public Object getValue(String name) {
        return deepSearch(name, getConfigJson());
    }

    public Object deepSearch(String name, JsonObject obj) {
        try {
            List<String> path = new LinkedList<>(Arrays.asList(name.split("\\.")));
            if(path.size() == 1) {
                return obj.get(path.get(0));
            } else {

                Object newValue = obj.get(path.get(0));
                path.remove(0);
                StringBuilder builder = new StringBuilder();
                path.forEach(s -> builder.append(s).append("."));
                builder.deleteCharAt(builder.toString().length() - 1).toString();
                return deepSearch(builder.toString(), gson.fromJson(gson.toJson(newValue), JsonObject.class));
            }
        } catch (Exception e) {
            return null;
        }
    }

}
