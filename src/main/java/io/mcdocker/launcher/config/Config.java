package io.mcdocker.launcher.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import io.mcdocker.launcher.utils.OSUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Config {

    private static final Config INSTANCE = new Config();
    public static Config getConfig() {return INSTANCE;}

    private final File configFile = new File(OSUtils.getUserData() + File.separator + "config.json");
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public void init() throws IOException {
        if(!configFile.exists()) configFile.createNewFile();

    }

    public void save(ConfigSerializer cfg) {
        try {
            FileWriter writer = new FileWriter(configFile);

//            writer.write(cf);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
