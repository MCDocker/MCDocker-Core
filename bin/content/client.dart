import 'version.dart';

abstract class Client {
  final Version version;
  final String url;

  Client(this.version, this.url);

  void installClient();
  void installLibraries();
  void installNatives();
  void installAssets();
}
