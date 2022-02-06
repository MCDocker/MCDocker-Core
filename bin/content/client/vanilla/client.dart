import 'dart:io';

import '../client.dart';
import '../client_manifest.dart';

class VanillaClient extends Client {
  VanillaClient(ClientManifest manifest) : super(manifest);

  @override
  Future<String> downloadAssets() {
    throw UnimplementedError();
  }

  @override
  Future<File> downloadClient() {
    throw UnimplementedError();
  }

  @override
  Future<List<String>> downloadLibraries() {
    throw UnimplementedError();
  }

  @override
  void downloadNatives(List<Map<String, dynamic>> natives) {
    // TODO: implement downloadNatives
  }

  @override
  String get typeName => throw UnimplementedError();
	
}