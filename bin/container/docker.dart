import 'package:uuid/uuid.dart';

import '../content/mod/mod.dart';

class DockerFile {
  String _name = "Unnamed";
  String _uuid = Uuid().v4();
  List<Mod> _mods = [];
  int _memory = 2048;
  var _client;
}
