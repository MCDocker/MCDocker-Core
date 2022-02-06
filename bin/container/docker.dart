import 'package:uuid/uuid.dart';

import '../content/client/client.dart';
import '../content/client/client_manifest.dart';
import '../content/mod/mod.dart';

class DockerFile {
  String name = "Unnamed";
  String uuid = Uuid().v4();
  List<Mod> mods = [];
  int memory = 2048;
  var client;

  DockerFile();

  DockerFile.fromJson(Map<String, dynamic> json)
      : name = json['name'],
        uuid = json['uuid'],
        mods = json['mods'],
        memory = json['memory'],
        client = json['client'];

  Map<String, dynamic> toJson() => {
        'name': name,
        'uuid': uuid,
        'mods': mods,
        'memory': memory,
        'client': client
      };
}
