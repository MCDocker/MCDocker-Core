package me.hottutorials.content;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.http.HTTPUtils;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.StreamSupport;

public class Version {

    private static final Gson gson = new Gson();
    private static final File javaFolder = new File(OSUtils.getUserData() + "java");
    private static final File versionsFolder = new File(OSUtils.getUserData() + "versions");
    private final static File librariesFolder = new File(OSUtils.getUserData() + "libraries");
    private final static File nativesFolder = new File(OSUtils.getUserData() + "natives");
    private final static File assetsFolder = new File(OSUtils.getUserData() + "assets");

    private final JsonObject manifest;

    public Version(JsonObject manifest) {
        this.manifest = manifest;
    }

    public String getName() {
        return manifest.get("id").getAsString();
    }

    public int getJavaVersion() {
        return manifest.has("javaVersion") ? manifest.get("javaVersion").getAsJsonObject().get("majorVersion").getAsInt() : 8;
    }

    public JsonObject getManifest() {
        return manifest;
    }

    @Override
    public String toString() {
        return getName();
    }

    public CompletableFuture<File> downloadJava() {
        if (!javaFolder.exists()) javaFolder.mkdirs();
        File javaVersionFolder = new File(javaFolder, getJavaVersion() + "");
        if (javaVersionFolder.exists())
            return CompletableFuture.completedFuture(new File(javaVersionFolder.listFiles()[0], "bin"));

        return CompletableFuture.supplyAsync(() -> {
            String tempOs = "linux";
            switch (OSUtils.getOperatingSystem()) {
                case MACOS:
                    tempOs = "mac";
                case WINDOWS:
                    tempOs = "windows";
            }
            final String os = tempOs;
            String tempArch = "x64";
            switch (OSUtils.getArchitecture()) { // TODO: Add more architectures.
                case "amd64":
                    tempArch = "x64";
            }
            final String arch = tempArch;
            JsonArray response = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL("https://api.adoptium.net/v3/assets/latest/" + getJavaVersion() + "/hotspot")
                    .setMethod(Method.GET).send(true), JsonArray.class);
            JsonObject binary = StreamSupport.stream(response.spliterator(), false).map(bin -> bin.getAsJsonObject().get("binary").getAsJsonObject())
                    .filter(bin -> (bin.get("image_type").getAsString().equals("jre") || bin.get("image_type").getAsString().equals("jdk")) && bin.get("os").getAsString().equalsIgnoreCase(os) && bin.get("architecture").getAsString().equalsIgnoreCase(arch))
                    .min((bin, bin2) -> {
                        if (bin.get("image_type").getAsString().equals("jre")) {
                            if (bin2.get("image_type").getAsString().equals("jre")) return 0;
                            else return -1;
                        } else return 1;
                    }).orElse(null);
            if (binary == null) return null;
            JsonObject pack = binary.get("package").getAsJsonObject();
            String fileName = pack.get("name").getAsString();
            javaVersionFolder.mkdirs();
            File archiveFile = new File(javaVersionFolder, fileName);
            HTTPUtils.download(pack.get("link").getAsString(), archiveFile);

            Archiver archiver = fileName.endsWith(".zip") ? ArchiverFactory.createArchiver("zip") : ArchiverFactory.createArchiver("tar", "gz");
            try {
                archiver.extract(archiveFile, javaVersionFolder);
                archiveFile.delete();
                return new File(javaVersionFolder.listFiles()[0], "bin");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    public CompletableFuture<File> downloadClient(ClientType type) {
        return CompletableFuture.supplyAsync(() -> {
            versionsFolder.mkdirs();
            File versionTypeFolder = new File(versionsFolder + "/" + type.name().toLowerCase());
            versionTypeFolder.mkdir();

            File client = new File(versionTypeFolder + "/" + getName() + ".jar");
            if (client.exists()) return client;

            String url = manifest.getAsJsonObject("downloads").getAsJsonObject("client").get("url").getAsString();

            HTTPUtils.download(url, client);

            return client;
        });
    }

    public CompletableFuture<List<String>> downloadLibraries() {
        return CompletableFuture.supplyAsync(() -> {
            librariesFolder.mkdir();

            JsonArray natives = new JsonArray();
            List<String> librariesList = new ArrayList<>();

            for (JsonElement library : manifest.getAsJsonArray("libraries")) {
                JsonObject lib = library.getAsJsonObject();
                if (lib.has("natives") || lib.has("extract")) {
                    natives.add(lib);
                    continue;
                }

                JsonObject artifact = lib.getAsJsonObject("downloads").getAsJsonObject("artifact");
                String path = artifact.get("path").getAsString();
                String url = artifact.get("url").getAsString();

                if (!librariesFolder.exists()) librariesFolder.mkdirs();
                File libraryFile = new File(librariesFolder, path);
                if (!libraryFile.getParentFile().exists()) libraryFile.getParentFile().mkdirs();
                if(!libraryFile.exists()) HTTPUtils.download(url, libraryFile);

                librariesList.add(libraryFile.getPath());
            }
            downloadNatives(natives);
            return librariesList;
        });
    }

    private void downloadNatives(JsonArray natives) {
        nativesFolder.mkdir();
        File currentNativeFolder = new File(nativesFolder + "/" + getName());
        if (currentNativeFolder.exists()) return;
        currentNativeFolder.mkdir();

        for (JsonElement nativ : natives) {
            JsonObject n = nativ.getAsJsonObject();
            boolean extract = n.has("extract") && n.has("natives");

            if (extract) {
                JsonObject classifiers = n.getAsJsonObject("downloads").getAsJsonObject("classifiers");

                JsonObject nativePlatform = null;

                switch (OSUtils.getOperatingSystem()) {
                    case WINDOWS:
                        if (classifiers.has("natives-windows"))
                            nativePlatform = classifiers.getAsJsonObject("natives-windows");
                        else
                            nativePlatform = classifiers.getAsJsonObject("natives-windows-" + (System.getenv("ProgramFiles(x86)") != null ? "64" : "32"));
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

                File nativeFile = new File(currentNativeFolder, outputFileName);
                if (nativeFile.exists()) continue;

                HTTPUtils.download(url, nativeFile);
                Archiver archiver = ArchiverFactory.createArchiver("jar");
                try {
                    archiver.extract(nativeFile, currentNativeFolder);
                    nativeFile.delete();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public CompletableFuture<String> downloadAssets() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (!assetsFolder.exists()) assetsFolder.mkdirs();
                File objectsFolder = new File(assetsFolder, "objects");
                if (!objectsFolder.exists()) objectsFolder.mkdirs();
                File indexesFolder = new File(assetsFolder, "indexes");
                if (!indexesFolder.exists()) indexesFolder.mkdirs();
                String indexId = manifest.get("assetIndex").getAsJsonObject().get("id").getAsString();
                File index = new File(indexesFolder, indexId + ".json");
                if (!index.exists()) HTTPUtils.download(manifest.get("assetIndex").getAsJsonObject().get("url").getAsString(), index);
                JsonObject objects = gson.fromJson(new FileReader(index), JsonObject.class).get("objects").getAsJsonObject();

                List<CompletableFuture<?>> futures = new ArrayList<>();
                objects.keySet().forEach(asset -> futures.add(CompletableFuture.runAsync(() -> {
                    try {
                        String hash = objects.get(asset).getAsJsonObject().get("hash").getAsString();
                        String shortHash = hash.substring(0, 2);
                        File hashFolder = new File(objectsFolder, shortHash);
                        if (!hashFolder.exists()) hashFolder.mkdirs();
                        File object = new File(hashFolder, hash);
                        if (!object.exists()) {
                            HTTPUtils.download("https://resources.download.minecraft.net/" + shortHash + "/" + hash, object);
                            if (indexId.equals("legacy") || indexId.equals("pre-1.6")) {
                                File virtualFolder = new File(assetsFolder, "virtual/" + indexId);
                                if (!virtualFolder.exists()) virtualFolder.mkdirs();
                                File mappedFile = new File(virtualFolder, asset);
                                if (!mappedFile.getParentFile().exists()) mappedFile.getParentFile().mkdirs();
                                if (!mappedFile.exists()) Files.copy(object.toPath(), mappedFile.toPath());
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                })));

                CompletableFuture.allOf(futures.toArray(new CompletableFuture<?>[]{})).join();
                return indexId;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

    // getVersions().get().get(0).get().get().get();

    public static CompletableFuture<List<MinVersion>> getVersions() {
        return CompletableFuture.supplyAsync(() -> {
            List<MinVersion> versions = new ArrayList<>();
            JsonArray versionArray = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL("https://launchermeta.mojang.com/mc/game/version_manifest.json")
                    .setMethod(Method.GET)
                    .send(), JsonObject.class).get("versions").getAsJsonArray();
            StreamSupport.stream(versionArray.spliterator(), false)
                    .map(JsonElement::getAsJsonObject)
                    .forEach(v -> versions.add(new MinVersion(v.get("id").getAsString(), () -> CompletableFuture.supplyAsync(() -> {
                        JsonObject version = gson.fromJson(RequestBuilder.getBuilder()
                                .setURL(v.get("url").getAsString())
                                .setMethod(Method.GET)
                                .send(), JsonObject.class);
                        return new Version(version);
                    }))));
            return versions;
        });
    }

    public static CompletableFuture<Optional<Version>> getVersion(String name) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return getVersions().get().stream().filter(version -> version.getName().equalsIgnoreCase(name)).findFirst().map(version -> {
                    try {
                        return version.get().get().get();
                    } catch (InterruptedException | ExecutionException e) {
                        return null;
                    }
                });
            } catch (InterruptedException | ExecutionException e) {
                return Optional.empty();
            }
        });
    }

}
