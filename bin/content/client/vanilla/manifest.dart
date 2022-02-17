import '../client_manifest.dart';
import 'client.dart';

class VanillaManifest extends ClientManifest {
  final String dataUrl;
  VanillaManifest({
    required this.dataUrl,
    required String name,
    required int javaVersion,
    required String mainClass,
    required String startupArguments,
    Type type = VanillaClient,
  }) : super(name, javaVersion, mainClass, startupArguments, type);
}
