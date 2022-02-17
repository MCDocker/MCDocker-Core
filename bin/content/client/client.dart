import 'dart:convert';
import 'dart:io';

import 'package:archive/archive_io.dart';
import 'package:http/http.dart';
import 'package:path/path.dart';

import '../../platform/platform_utils.dart';
import '../../utils/http_wrapper.dart';
import 'client_manifest.dart';

abstract class Client<T extends ClientManifest> {
  static final Directory javaFolder =
      Directory(join(PlatformUtils().wrapper.mcdockerPath, "java"));
  static final Directory librariesFolder =
      Directory(join(PlatformUtils().wrapper.mcdockerPath, "libraries"));
  static final Directory nativesFolder =
      Directory(join(PlatformUtils().wrapper.mcdockerPath, "natives"));
  static final Directory assetsFolder =
      Directory(join(PlatformUtils().wrapper.mcdockerPath, "assets"));

  final T manifest;

  Client(this.manifest);

  String get typeName;

  Future<File> downloadJava() {
    if (!javaFolder.existsSync()) javaFolder.createSync();
    Directory javaVersionFolder =
        Directory(join(javaFolder.path, manifest.javaVersion.toString()));

    if (javaVersionFolder.existsSync() &&
        Directory(join(javaVersionFolder.listSync()[0].path, 'bin'))
            .existsSync()) {
      return Future.value(
          File(join(javaVersionFolder.listSync()[0].path, 'bin')));
    }

    return Future(() async {
      String tempOs = "linux";
      switch (Platform.operatingSystem) {
        case "windows":
          tempOs = "windows";
          break;
        case "macos":
          tempOs = "macos";
          break;
      }
      final String os = tempOs;

      String tempArch = "x64";
      // switch(SysInfo.cores[0].architecture) {
      //   case ProcessorArchitecture.arm
      // }

      final String arch = tempArch;
      Response response = await get(Uri.parse(
          "https://api.adoptium.net/v3/assets/latest/${manifest.javaVersion}/hotspot"));

      List<dynamic> json = jsonDecode(response.body);

      Map<String, dynamic> binary = {};
      for (var el in json) {
        if ((el['binary']['image_type'] == 'jre' ||
                el['binary']['image_type'] == 'jdk') &&
            el['binary']['architecture'] == arch &&
            el['binary']['os'] == os) {
          binary = el['binary'];
          break;
        }
      }

      if (binary.isEmpty) return Future.error("No binary found");

      String fileName = binary['package']['name'].toString();
      javaVersionFolder.createSync();
      File archiveFile = File(join(javaVersionFolder.path, fileName));
      await HTTPUtils.downloadFile(
          binary['package']['link'].toString(), archiveFile);

      final inputStream = InputFileStream(archiveFile.path);
      try {
        final archive = ZipDecoder().decodeBuffer(inputStream);
        extractArchiveToDisk(archive, javaVersionFolder.path);
        return File(join(javaVersionFolder.listSync()[0].path, "bin"));
      } catch (e) {
        print(e);
      }

      return Future.value();
    });
  }

  Future<File> downloadClient();
  Future<List<String>> downloadLibraries();
  void downloadNatives(List<dynamic> natives);
  Future<String> downloadAssets();
}
