import 'dart:io';

import '../platform.dart';

class WindowsWrapper extends PlatformWrapper {
  @override
  void openUrl(String uri) {
    Process.run("explorer \"$uri\"", []);
  }

  @override
  String get minecraftPath =>
      '${Platform.environment['APPDATA']}\\.minecraft\\';

  @override
  String get mcdockerPath => '${Platform.environment['APPDATA']}\\.mcdocker\\';
}
