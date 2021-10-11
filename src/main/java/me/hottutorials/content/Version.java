package me.hottutorials.content;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.http.HTTPUtils;
import me.hottutorials.utils.http.Method;
import me.hottutorials.utils.http.RequestBuilder;
import org.rauschig.jarchivelib.Archiver;
import org.rauschig.jarchivelib.ArchiverFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

public class Version {

    private static final Gson gson = new Gson();
    private static final File javaFolder = new File(OSUtils.getUserData() + "java");

    private final String name;
    private final int javaVersion;

    public Version(String name, int javaVersion) {
        this.name = name;
        this.javaVersion = javaVersion;
    }

    public String getName() {
        return name;
    }

    public int getJavaVersion() {
        return javaVersion;
    }

    @Override
    public String toString() {
        return name;
    }

    public CompletableFuture<File> downloadJava() {
        if (!javaFolder.exists()) javaFolder.mkdirs();
        File javaVersionFolder = new File(javaFolder, javaVersion + "");
        if (javaVersionFolder.exists()) return CompletableFuture.completedFuture(new File(Arrays.stream(javaVersionFolder.listFiles()).findFirst().get(), "bin"));

        return CompletableFuture.supplyAsync(() -> {
            String tempOs = "linux";
            switch (OSUtils.getOperatingSystem()) {
                case MACOS: tempOs = "mac";
                case WINDOWS: tempOs = "windows";
            }
            final String os = tempOs;
            String tempArch = "x64";
            switch (OSUtils.getArchitecture()) { // TODO: Add more architectures.
                case "amd64": tempArch = "x64";
            }
            final String arch = tempArch;
            JsonArray response = gson.fromJson(RequestBuilder.getBuilder()
                    .setURL("https://api.adoptium.net/v3/assets/latest/" + javaVersion + "/hotspot")
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
            HTTPUtils.download(pack.get("link").getAsString(), archiveFile.getPath());

            Archiver archiver = fileName.endsWith(".zip") ? ArchiverFactory.createArchiver("zip") : ArchiverFactory.createArchiver("tar", "gz");
            try {
                archiver.extract(archiveFile, javaVersionFolder);
                archiveFile.delete();
                return new File(Arrays.stream(javaVersionFolder.listFiles()).findFirst().get(), "bin");
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
    }

}
