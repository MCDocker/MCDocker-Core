import 'dart:io';

import '../platform.dart';

class LinuxWrapper extends PlatformWrapper {
  @override
  void openUrl(String uri) {
    Process.run("x-www-browser", [uri]);
  }

  @override
  String get minecraftPath =>
      '${Platform.environment['HOME']}/.config/.minecraft/';

  @override
  String get mcdockerPath =>
      '${Platform.environment['HOME']}/.config/.mcdocker/';
}
