import 'dart:io';

import '../platform.dart';

class MacWrapper extends PlatformWrapper {
  @override
  void openUrl(String uri) {
    Process.run("open", [uri]);
  }

  @override
  String get minecraftPath =>
      '${Platform.environment['HOME']}/Library/Application Support/minecraft/';

  @override
  String get mcdockerPath =>
      '${Platform.environment['HOME']}/Library/Application Support/mcdocker/';
}
