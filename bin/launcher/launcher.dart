import 'dart:io';

import 'package:path/path.dart';

import '../auth/account.dart';
import '../container/container.dart';
import '../content/client/client.dart';
import '../platform/platform_utils.dart';

class Launcher {
  static final File versionsFolder =
      File(join(PlatformUtils().mcdockerDir.path, "versions"));
  static final File nativesFolder =
      File(join(PlatformUtils().mcdockerDir.path, "natives"));
  static final File assetsFolder =
      File(join(PlatformUtils().mcdockerDir.path, "assets"));

  final Container container;
  final Client client;

  Launcher(this.container, this.client);

  Future<Process> launch(Account account) {
    return Future((() {
      try {
        String javaPath = "java";
        String index = "";
        List<String> librariesList = [];

        Stopwatch stopwatch = Stopwatch()..start();

        List<Future> futures = [
          client.downloadJava().then((bin) {
            if (bin.existsSync() || bin != null) {
              javaPath = bin.path + separator + "java";
            }
          }),
          client.downloadClient().then((client) {}),
          client.downloadLibraries().then((libraries) {
            librariesList.addAll(libraries);
          }),
          client.downloadAssets().then((i) {
            if (i != null) index = i;
          })
        ];
        Future.wait(futures);
        stopwatch.stop();
        print("Finished downloading in ${stopwatch.elapsed}");

        List<String> librariesBuilder = [];
        librariesList.forEach((s) => librariesBuilder.addAll(
            [s.replaceAll("\\", "/"), (Platform.isWindows ? ";" : ":")]));
        if (librariesBuilder.isEmpty) {
          throw Exception("Libraries lenght is 0");
        }

        String libraries = librariesBuilder
            .join("")
            .substring(0, librariesBuilder.join("").length - 1);

        String arguments = client.manifest.startupArguments
            .replaceAll(r"${auth_player_name}", account.username)
            .replaceAll(r"${version_name}", client.manifest.name)
            .replaceAll(r"${game_directory}", container.folder.path)
            .replaceAll(r"${assets_root}", assetsFolder.path)
            .replaceAll(
                r"${game_assets}", assetsFolder.path + "/virtual/" + index)
            .replaceAll(r"${assets_index_name}", index)
            .replaceAll(r"${auth_uuid}", account.uuid)
            .replaceAll(r"${auth_access_token}", account.accessToken)
            .replaceAll(r"${auth_session}", "0")
            .replaceAll(r"${user_properties}", "{}")
            .replaceAll(r"${user_type}", "1")
            .replaceAll(r"${natives}",
                nativesFolder.path + "/" + client.manifest.name + "/")
            .replaceAll(r"${libraries}", libraries)
            .replaceAll(r"${min_memory}", "512")
            .replaceAll(
                r"${max_memory}", container.dockerfile.memory.toString())
            .replaceAll(r"${main_class}", client.manifest.mainClass)
            .replaceAll(r"${version_type}", "MCDocker");

        return Process.start(javaPath, [arguments]);
      } catch (e) {
        print(e);
        return Future.error(e);
      }
    }));
  }
}
