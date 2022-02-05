import 'dart:io';

import 'impl/linux.dart';
import 'impl/macos.dart';
import 'platform.dart';
import 'impl/win.dart';

class PlatformUtils {
  late PlatformWrapper _wrapper;

  PlatformUtils() {
    switch (Platform.operatingSystem) {
      case "windows":
        _wrapper = WindowsWrapper();
        break;
      case "macos":
        _wrapper = MacWrapper();
        break;
      case "linux":
      default:
        _wrapper = LinuxWrapper();
        break;
    }
  }

  PlatformWrapper get wrapper => _wrapper;
  Directory get mcdockerDir => Directory(wrapper.mcdockerPath);
}
