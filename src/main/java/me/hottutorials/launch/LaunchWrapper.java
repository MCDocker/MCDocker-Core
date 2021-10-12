package me.hottutorials.launch;

import me.hottutorials.auth.Account;
import me.hottutorials.content.ClientType;
import me.hottutorials.content.Version;
import me.hottutorials.utils.Logger;
import me.hottutorials.utils.OSUtils;
import me.hottutorials.utils.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class LaunchWrapper {

    private static final File versionsFolder = new File(OSUtils.getUserData() + "versions");
    private final static File nativesFolder = new File(OSUtils.getUserData() + "natives");

    private final ClientType type;
    private final Version version;

    public LaunchWrapper(Version version, ClientType type) throws IOException {
        this.type = type;
        this.version = version;
    }

    public CompletableFuture<Process> launch(Account account, String... arguments) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                AtomicReference<String> javaPath = new AtomicReference<>("java");
                List<String> librariesList = new ArrayList<>();

                long start = System.currentTimeMillis();

                CompletableFuture.allOf(
                        version.downloadJava().thenAccept(binFile -> {
                            if (binFile != null) javaPath.set(binFile.getPath() + File.separator + "java");
                        }),
                        version.downloadClient(type),
                        version.downloadLibraries().thenAccept(librariesList::addAll)
                ).join();

                long end = System.currentTimeMillis();
                Logger.log(StringUtils.format("Finished downloading in ${0}ms.", end - start));

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

                // TODO: Parse args from manifest.
                args.add("-Xmx3G");
                args.add("net.minecraft.client.main.Main");
                args.add("--username " + account.getUsername());
                args.add("--uuid " + account.getUniqueId());
                args.add("--version " + version);
                args.add("--accessToken " + account.getAccessToken());
                args.add("--userProperties {}");

                StringBuilder argsBuilder = new StringBuilder();
                args.forEach(arg -> argsBuilder.append(arg).append(" "));
                args.addAll(Arrays.asList(arguments));

                return Runtime.getRuntime().exec(javaPath.get() + " " + argsBuilder);
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        });
    }



}
