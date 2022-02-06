import 'dart:convert';
import 'dart:io';

import 'package:path/path.dart';

import '../platform/platform_utils.dart';
import 'docker.dart';

class Container {
  static final Directory containersFolder =
      Directory(join(PlatformUtils().wrapper.mcdockerPath, "containers"));
  static final String dockerFileName = 'mcdocker.json';

  late final Directory folder;
  final DockerFile dockerfile;
  late final File dockerFile;

  Container(this.dockerfile) {
    folder = Directory(join(containersFolder.path, dockerfile.uuid.toString()));
    dockerFile = File(join(folder.path, dockerFileName));
  }

  void saveSync() =>
      dockerFile.writeAsStringSync(jsonEncode(dockerfile.toJson()));
  void save() => dockerFile.writeAsString(jsonEncode(dockerfile.toJson()));

  void deleteSync() => folder.deleteSync(recursive: true);
  void delete() => folder.delete(recursive: true);

  static Container? fromFolder(Directory folder) {
    File dockerFile = File(join(folder.path, dockerFileName));
    if (dockerFile.existsSync()) return fromFile(dockerFile);
    return null;
  }

  static Container fromFile(File dockerFile) {
    return fromDockerFile(
        DockerFile.fromJson(jsonDecode(dockerFile.readAsStringSync())));
  }

  static Container fromDockerFile(DockerFile dockerfile) {
    return Container(dockerfile);
  }

  static Container? getContainerById(String id) {
    for (Container container in getContainers()) {
      if (container.dockerfile.uuid == id) return container;
    }
    return null;
  }

  static List<Container> getContainers() {
    List<Container> containers = [];
    if (containersFolder.existsSync()) {
      for (int i = 0; i < containersFolder.listSync().length; i++) {
        FileSystemEntity fse = containersFolder.listSync()[i];
        if (fse.statSync().type != FileSystemEntityType.directory) continue;

        try {
          Directory folder = fse as Directory;
          Container? container = fromFolder(folder);
          if (container != null) containers.add(container);
        } catch (e) {
          print(e);
        }
      }
    } else {
      containersFolder.createSync();
    }
    return containers;
  }
}
