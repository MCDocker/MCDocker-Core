package me.hottutorials.launch;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import me.hottutorials.content.ClientType;
import me.hottutorials.utils.FSUtils;
import me.hottutorials.utils.Logger;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.http.HTTPUtils;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class LaunchWrapper {

    private JsonObject versionManifest = null;
    private final File versionManifestFolder = new File(OSUtils.getUserData() + "manifests");
    private final File versionsFolder = new File(OSUtils.getUserData() + "versions");
    private final File nativesFolder = new File(OSUtils.getUserData() + "natives");
    private final File librariesFolder = new File(OSUtils.getUserData() + "libraries");

    private final ClientType type;
    private final String version;

    public LaunchWrapper(String version, ClientType type, String[] arguments) {
        Logger.log("Launch Wrapper Called");

        this.type = type;
        this.version = version;

        try {
            versionsFolder.mkdir();

            Logger.debug("Setting version manifest");
            versionManifestFolder.mkdir();
            setVersionManifest();

            Logger.debug("Preparing for client download");
            downloadClient();
            Logger.log("Finished downloading client");

            Logger.debug("Preparing library download");
            downloadLibraries();
            Logger.log("Finished downloading libraries");



            List<String> args = new ArrayList<>();
            args.add("-XX:-UseAdaptiveSizePolicy");
            args.add("-XX:-OmitStackTraceInFastThrow");
            args.add("-Dfml.ignorePatchDiscrepancies=true");
            args.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
            args.add("-Dminecraft.launcher.brand=mc-docker");

            args.add("-Djava.library.path=");
        } catch (Exception e) {
            Logger.log(e);
        }

    }


    private void downloadNatives(JsonArray natives) {
        nativesFolder.mkdir();

        for(JsonElement nativ : natives) {
            JsonObject n = nativ.getAsJsonObject();

            boolean extract = false;

            if(n.has("extract") && n.has("natives")) extract = true;
//            if(n.has("rules")) {
//                List<Map<String, JsonElement>> rules = new ArrayList<>();
//
//                for(JsonElement rul : n.getAsJsonArray("rules")) {
//                    JsonObject rule = rul.getAsJsonObject();
//                    Map<String, JsonElement> rools = new HashMap<>();
//                    for(Map.Entry<String, JsonElement> entry : rule.entrySet()) {
//                        rools.put(entry.getKey(), entry.getValue());
//                    }
//                    rules.add(rools);
//                }
//
//                for(Map<String, JsonElement> map : rules) {
//                    for(Map.Entry<String, JsonElement> entry : map.entrySet()) {
//                        boolean action = false;
//                        String os = null;
//                        if(entry.getKey().equalsIgnoreCase("action")) {
//                            if(entry.getValue().getAsString().equalsIgnoreCase("allow")) action = true;
//                            else action = false;
//                        }
//
//                        if(entry.getKey().equalsIgnoreCase("os"))
//                            os = entry.getValue().getAsJsonObject().get("name").getAsString();
//
//                        if(action) {
//                            switch (OSUtils.getOperatingSystem()) {
//                                case LINUX:
//
//                            }
//                        }
//
//                    }
//                }
//
//            }

            if(extract) {

                JsonObject classifiers = n.getAsJsonObject("downloads").getAsJsonObject("classifiers");
                JsonObject nativePlatform = null;


                switch (OSUtils.getOperatingSystem()) {
                    case WINDOWS:
                        nativePlatform = classifiers.getAsJsonObject("natives-windows");
                        break;
                    case LINUX:
                        nativePlatform = classifiers.getAsJsonObject("natives-linux");
                        break;
                    case MACOS:
                        nativePlatform = classifiers.getAsJsonObject("natives-osx");
                        break;
                    case OTHER:
                        System.exit(1);
                        break;
                }

                String path = nativePlatform.get("path").getAsString();
                String url = nativePlatform.get("url").getAsString();
                long size = nativePlatform.get("size").getAsLong();

                String foldersPath = path.substring(0, path.length() - path.split("/")[path.split("/").length - 1].length());
                String outputFileName = path.split("/")[path.split("/").length - 1];

                FSUtils.createDirRecursively(nativesFolder.getAbsolutePath(), foldersPath);
                HTTPUtils.download(url, nativesFolder + "/" + path);
            }

        }
    }

    private void downloadLibraries() throws InterruptedException {
        CompletableFuture.runAsync(() -> {
            librariesFolder.mkdir();

            JsonArray natives = new JsonArray();

            for(JsonElement library : versionManifest.getAsJsonArray("libraries")) {
                JsonObject lib = library.getAsJsonObject();
                if(lib.has("natives") || lib.has("rules") || lib.has("extract")) {
                    natives.add(lib);
                    continue;
                }

                String name = lib.get("name").getAsString();
                JsonObject artifact = lib.getAsJsonObject("downloads").getAsJsonObject("artifact");
                String path = artifact.get("path").getAsString();
                String url = artifact.get("url").getAsString();
                long size = artifact.get("size").getAsLong();

                String foldersPath = path.substring(0, path.length() - path.split("/")[path.split("/").length - 1].length());
                String outputFileName = path.split("/")[path.split("/").length - 1];

//                FSUtils.createDirRecursively(librariesFolder.getAbsolutePath(), foldersPath);
//                HTTPUtils.download(url, librariesFolder + "/" + path);
            }

            downloadNatives(natives);

        });
    }

    private void downloadClient() {
        long size = versionManifest.getAsJsonObject("downloads").getAsJsonObject("client").get("size").getAsLong();
        long sizeMB = size / 1000 / 1000;
        String url = versionManifest.getAsJsonObject("downloads").getAsJsonObject("client").get("url").getAsString();

        File versionTypeFolder = new File(versionsFolder + "/" + type.name().toLowerCase());
        versionTypeFolder.mkdir();
        File client = new File(versionTypeFolder + "/" + version + ".jar");

        HTTPUtils.download(url, client.getAbsolutePath());
    }


    private void setVersionManifest() throws IOException {
        File manifestFile = new File(versionManifestFolder + "/" + version + ".manifest.json");

        if(manifestFile.exists()) {
            Logger.debug("Reading manifest from file: " + manifestFile);
            JsonReader reader = new JsonReader(new FileReader(manifestFile));
            versionManifest = new Gson().fromJson(reader, JsonObject.class);
        } else {
            Logger.debug("No manifest saved");
            JsonArray versions = getVersions();
            for(JsonElement ver : versions) {
                JsonObject v = ver.getAsJsonObject();
                if(v.has("id") && v.get("id").getAsString().equalsIgnoreCase(version)) {
                    String res = RequestBuilder.getBuilder()
                            .setURL(v.get("url").getAsString())
                            .setMethod(Method.GET).send();

                    versionManifest = new Gson().fromJson(res, JsonObject.class);

                    FileWriter writer = new FileWriter(manifestFile);
                    writer.write(res);
                    writer.flush();
                    writer.close();

                    Logger.debug("Saved manifest to: " + manifestFile);

                }
            }
        }
    }

    private JsonArray getVersions() {
        String res = RequestBuilder.getBuilder()
                .setURL("https://launchermeta.mojang.com/mc/game/version_manifest.json")
                .setMethod(Method.GET)
                .send();

        return new Gson().fromJson(res, JsonObject.class).get("versions").getAsJsonArray();
    }

}
