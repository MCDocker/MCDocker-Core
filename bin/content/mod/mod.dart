import 'mod_manifest.dart';

class Mod<T extends ModManifest> {
  final T _manifest;

  Mod(this._manifest);

  get manifest => _manifest;

  get name => _manifest.name;
}
