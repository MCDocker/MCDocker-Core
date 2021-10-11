package me.hottutorials.launch;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import me.hottutorials.auth.Account;
import me.hottutorials.content.ClientType;
import me.hottutorials.utils.FSUtils;
import me.hottutorials.utils.Logger;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.StringUtils;
import me.hottutorials.utils.http.HTTPUtils;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class LaunchWrapper {

    private static final File versionsFolder = new File(OSUtils.getUserData() + "versions");
    private final static File nativesFolder = new File(OSUtils.getUserData() + "natives");
    private final static File librariesFolder = new File(OSUtils.getUserData() + "libraries");

    private boolean downloaded = false;
    private final JsonObject versionManifest;

    private final ClientType type;
    private final String version;

    private final List<String> librariesList = new ArrayList<>();

    public LaunchWrapper(String version, ClientType type) throws IOException {
        this.type = type;
        this.version = version;

        versionsFolder.mkdir();

        Logger.debug("Setting version manifest");
        File versionManifestFolder = new File(OSUtils.getUserData() + "manifests");
        File manifestFile = new File(versionManifestFolder + "/" + version + ".manifest.json");

        JsonObject tempVersionManifest = null;
        if (manifestFile.exists()) {
            Logger.debug("Reading manifest from file: " + manifestFile);
            JsonReader reader = new JsonReader(new FileReader(manifestFile));
            tempVersionManifest = new Gson().fromJson(reader, JsonObject.class);
        } else {
            Logger.debug("No manifest saved");
            JsonArray versions = getVersions();
            for (JsonElement ver : versions) {
                JsonObject v = ver.getAsJsonObject();
                if(v.has("id") && v.get("id").getAsString().equalsIgnoreCase(version)) {
                    String res = RequestBuilder.getBuilder()
                            .setURL(v.get("url").getAsString())
                            .setMethod(Method.GET).send();

                    tempVersionManifest = new Gson().fromJson(res, JsonObject.class);

                    FileWriter writer = new FileWriter(manifestFile);
                    writer.write(res);
                    writer.flush();
                    writer.close();

                    Logger.debug("Saved manifest to: " + manifestFile);
                    break;
                }
            }
        }
        this.versionManifest = tempVersionManifest;
    }

    public CompletableFuture<Process> launch(Account account, String... arguments) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!downloaded) download().get();

                List<String> args = new ArrayList<>();
                args.add("-XX:-UseAdaptiveSizePolicy");
                args.add("-XX:-OmitStackTraceInFastThrow");
                args.add("-Dfml.ignorePatchDiscrepancies=true");
                args.add("-Dfml.ignoreInvalidMinecraftCertificates=true");
                args.add("-Dminecraft.launcher.brand=mc-docker");

                args.add("-Djava.library.path=" + nativesFolder + "/" + version + "/");
                args.add("-Dminecraft.client.jar=" + versionsFolder + "/" + type.name().toLowerCase() + "/" + version + ".jar");

                StringBuilder librariesBuilder = new StringBuilder();
                librariesList.forEach(s -> librariesBuilder.append(s.replace("\\", "/")).append(";"));
                if (librariesBuilder.length() == 0) throw new Exception("Libraries length is 0");
                librariesBuilder.deleteCharAt(librariesBuilder.toString().length() - 1);
                args.add("-cp \"" + librariesBuilder + ";" + versionsFolder.getPath().replace("\\", "/") + "/" + type.name().toLowerCase() + "/" + version + ".jar\"");

                args.add("-Xmx3G");
                args.add("net.minecraft.client.main.Main");
                args.add("--username " + account.getUsername());
                args.add("--version " + version);
                args.add("--accessToken 0"); // TODO: Make this work.
                args.add("--userProperties {}");

                StringBuilder argsBuilder = new StringBuilder();
                args.forEach(arg -> argsBuilder.append(arg).append(" "));
                args.addAll(Arrays.asList(arguments));

                return Runtime.getRuntime().exec("java " + argsBuilder);
            } catch (Exception e) {
                return null;
            }
        });
    }

    public CompletableFuture<Void> download() {
        return CompletableFuture.runAsync(() -> {
            long start = System.currentTimeMillis();

            Logger.debug("Preparing for client download");
            downloadClient();
            Logger.log("Finished downloading client");

            Logger.debug("Preparing library download");
            JsonArray natives = downloadLibraries();
            Logger.log("Finished downloading libraries");

            Logger.debug("Preparing natives download");
            downloadNatives(natives);
            Logger.log("Finished downloading natives");

            downloaded = true;

            long end = System.currentTimeMillis();
            Logger.log(StringUtils.format("Finished downloading in ${0}ms.", end - start));
        });
    }

    private void downloadNatives(JsonArray natives) {
        nativesFolder.mkdir();
        File currentNativeFolder = new File(nativesFolder + "/" + version);
        currentNativeFolder.mkdir();

        for(JsonElement nativ : natives) {
            JsonObject n = nativ.getAsJsonObject();
            boolean extract = n.has("extract") && n.has("natives");

            if (extract) {
                JsonObject classifiers = n.getAsJsonObject("downloads").getAsJsonObject("classifiers");

                JsonObject nativePlatform = null;

                switch (OSUtils.getOperatingSystem()) {
                    case WINDOWS:
                        if(classifiers.has("natives-windows")) nativePlatform = classifiers.getAsJsonObject("natives-windows");
                        else nativePlatform = classifiers.getAsJsonObject("natives-windows-" + (System.getenv("ProgramFiles(x86)") != null ? "64" : "32"));
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

                if (nativePlatform == null) continue;

                String path = nativePlatform.get("path").getAsString();
                String url = nativePlatform.get("url").getAsString();

                String outputFileName = path.split("/")[path.split("/").length - 1];

                if(new File(currentNativeFolder + "/" + outputFileName).exists()) continue;

                HTTPUtils.download(url, currentNativeFolder + "/" + outputFileName);
            }
        }
    }

    private JsonArray downloadLibraries() {
        librariesFolder.mkdir();

        JsonArray natives = new JsonArray();

        for(JsonElement library : versionManifest.getAsJsonArray("libraries")) {
            JsonObject lib = library.getAsJsonObject();
            if(lib.has("natives") || lib.has("extract")) {
                natives.add(lib);
                continue;
            }

            JsonObject artifact = lib.getAsJsonObject("downloads").getAsJsonObject("artifact");
            String path = artifact.get("path").getAsString();
            String url = artifact.get("url").getAsString();

            if(!new File(librariesFolder + "/" + path).exists()) {
                String foldersPath = path.substring(0, path.length() - path.split("/")[path.split("/").length - 1].length());

                FSUtils.createDirRecursively(librariesFolder.getAbsolutePath(), foldersPath);
                HTTPUtils.download(url, librariesFolder + "/" + path);
            }

            librariesList.add(librariesFolder + "/" + path);
        }
        return natives;
    }

    private void downloadClient() {
        File versionTypeFolder = new File(versionsFolder + "/" + type.name().toLowerCase());
        versionTypeFolder.mkdir();

        File client = new File(versionTypeFolder + "/" + version + ".jar");
        if(client.exists()) return;

        String url = versionManifest.getAsJsonObject("downloads").getAsJsonObject("client").get("url").getAsString();

        HTTPUtils.download(url, client.getAbsolutePath());
    }

    private JsonArray getVersions() {
        String res = RequestBuilder.getBuilder()
                .setURL("https://launchermeta.mojang.com/mc/game/version_manifest.json")
                .setMethod(Method.GET)
                .send();

        return new Gson().fromJson(res, JsonObject.class).get("versions").getAsJsonArray();
    }

}
