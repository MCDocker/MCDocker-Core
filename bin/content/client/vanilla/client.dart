import 'dart:convert';
import 'dart:io';

import 'package:archive/archive_io.dart';
import 'package:path/path.dart';

import '../../../platform/platform_utils.dart';
import '../../../utils/NumberUtils.dart';
import '../../../utils/http_wrapper.dart';
import '../client.dart';
import 'manifest.dart';

class VanillaClient extends Client<VanillaManifest> {
  final Map<String, dynamic> _data;
  late final Directory versionsFolder =
      Directory(join(PlatformUtils().mcdockerDir.path, "versions", typeName));

  VanillaClient(VanillaManifest manifest, this._data) : super(manifest);

  @override
  Future<String> downloadAssets() {
    return Future((() async {
      try {
        if (!Client.assetsFolder.existsSync()) Client.assetsFolder.createSync();
        Directory objectsFolder =
            Directory(join(Client.assetsFolder.path, "objects"));
        if (!objectsFolder.existsSync()) objectsFolder.createSync();
        Directory indexesFolder =
            Directory(join(Client.assetsFolder.path, "indexes"));
        if (!indexesFolder.existsSync()) indexesFolder.createSync();

        String indexId = _data['assetIndex']['id'];
        File index = File(join(indexesFolder.path, indexId + ".json"));
        if (!index.existsSync()) {
          await HTTPUtils.downloadFile(_data['assetIndex']['url'], index);
        }
        Map<String, dynamic> objects =
            jsonDecode(index.readAsStringSync())['objects'];

        List<Future> futures = [];
        objects.keys.forEach((asset) => futures.add(Future((() async {
              try {
                String hash = objects[asset]['hash'];
                String shortHash = hash.substring(0, 2);
                Directory hashFolder =
                    Directory(join(objectsFolder.path, shortHash));
                File object = File(join(hashFolder.path, hash));
                if (!object.existsSync()) {
                  await HTTPUtils.downloadFile(
                      "https://resources.download.minecraft.net/$shortHash/$hash",
                      object);
                }

                if (indexId == 'legacy' || indexId == 'pre-1.6') {
                  Directory virtualFolder = Directory(
                      join(Client.assetsFolder.path, 'virtual/$indexId'));
                  if (!virtualFolder.existsSync()) virtualFolder.createSync();
                  File mappedFile = File(join(virtualFolder.path, asset));
                  if (!mappedFile.parent.existsSync()) {
                    mappedFile.parent.createSync();
                  }
                  if (!mappedFile.existsSync()) {
                    object.copySync(mappedFile.path);
                  }
                }
              } catch (e) {
                print(e);
              }
            }))));
        Future.wait(futures);
        return indexId;
      } catch (e) {
        print(e);
        return "";
      }
    }));
  }

  @override
  Future<File> downloadClient() {
    return Future((() async {
      versionsFolder.createSync(recursive: true);
      File client = File(join(versionsFolder.path, manifest.name + ".jar"));
      if (client.existsSync()) return client;

      String url = _data['downloads']['client']['url'];

      await HTTPUtils.downloadFile(url, client,
          progressBar: true, status: "Downloading " + manifest.name);

      return client;
    }));
  }

  @override
  Future<List<String>> downloadLibraries() {
    return Future(() async {
      Client.librariesFolder.createSync();
      List<dynamic> natives = [];
      List<String> librariesList = [];
      Map<String, String> librariesMap = {};
      for (Map<String, dynamic> lib in _data['libraries']) {
        if (lib.containsKey("natives") || lib.containsKey("extract")) {
          natives.add(lib);
          continue;
        }

        Map<String, dynamic> artifact = lib['downloads']['artifact'];
        String path = artifact['path'];
        String url = artifact['url'];
        String name = lib['name'];
        String libraryName = name.split(":")[name.split(":").length - 2];
        String version = name.split(":")[name.split(":").length - 1];

        if (librariesMap.containsKey(libraryName) &&
            NumberUtils.parseInt(
                    librariesMap[libraryName]!.replaceAll("\\.", "")) !=
                null &&
            NumberUtils.parseInt(version.replaceAll("\\.", "")) != null &&
            NumberUtils.parseInt(
                    librariesMap[libraryName]!.replaceAll("\\.", ""))! <=
                NumberUtils.parseInt(version.replaceAll("\\.", ""))!) {
          String pathToRemove =
              "${Client.librariesFolder.path}/${path.replaceAll(version, librariesMap[libraryName]!)}";
          librariesMap.remove(libraryName);
          librariesList.remove(pathToRemove);
        }

        librariesMap.addAll({libraryName: version});
        if (!Client.librariesFolder.existsSync()) {
          Client.librariesFolder.createSync();
        }
        File libraryFile = File(join(Client.librariesFolder.path, path));
        if (!libraryFile.parent.existsSync()) libraryFile.parent.createSync();
        if (!libraryFile.existsSync()) {
          await HTTPUtils.downloadFile(url, libraryFile);
        }
        librariesList.add(libraryFile.path);
      }
      File clientFile = File(join(versionsFolder.path, manifest.name + ".jar"));
      if (!clientFile.existsSync()) {
        throw Exception("Client file does not exist");
      }

      librariesList.add(clientFile.path);
      downloadNatives(natives);
      return librariesList;
    });
  }

  @override
  void downloadNatives(List<dynamic> natives) async {
    Client.nativesFolder.createSync();
    Directory currentNativeFolder =
        Directory(join(Client.nativesFolder.path, manifest.name));
    if (currentNativeFolder.existsSync()) {
      return;
    } // TODO: Make it still check if every native is downloaded correctly
    currentNativeFolder.createSync();

    for (Map<String, dynamic> n in natives) {
      bool extract = n.containsKey("extract") || n.containsKey("natives");

      if (extract) {
        Map<String, dynamic> classifiers = n['downloads']['classifiers'];
        Map<String, dynamic>? nativePlatform;

        switch (Platform.operatingSystem) {
          case "windows":
            if (classifiers.containsKey("natives-windows")) {
              nativePlatform = classifiers['natives-windows'];
            } else {
              nativePlatform = classifiers[
                  'natives-windows-${Platform.environment["ProgramFiles(x86)"] != null ? "64" : "32"}'];
            }
            break;

          case "linux":
            nativePlatform = classifiers['natives-linux'];
            break;
          case "macos":
            nativePlatform = classifiers['natives-osx'];
            break;
          default:
            exit(1);
        }

        if (nativePlatform == null) continue;

        String path = nativePlatform['path'];
        String url = nativePlatform['url'];

        String outputFileName = path.split("/")[path.split("/").length - 1];
        File nativeFile = File(join(currentNativeFolder.path, outputFileName));
        if (nativeFile.existsSync()) continue;
        await HTTPUtils.downloadFile(url, nativeFile);
        final inputStream = InputFileStream(nativeFile.path);
        final archive = ZipDecoder().decodeBuffer(inputStream);
        try {
          extractArchiveToDisk(archive, currentNativeFolder.path);
          nativeFile.deleteSync();
        } catch (e) {
          print(e);
        }
      }
    }
  }

  @override
  String get typeName => "vanilla";
}
