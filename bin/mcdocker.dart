import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart';
import 'package:system_info2/system_info2.dart';

import 'commands/manager.dart';
import 'content/client/client_manifest.dart';
import 'content/client/vanilla/client.dart';
import 'platform/platform_utils.dart';
import 'utils/color.dart';
import 'utils/progress_bar.dart';

String apiUrl = "http://20.113.69.92:8080/api/";

Future<void> main(List<String> arguments) async {
  if (arguments.isNotEmpty && arguments[0].startsWith("mcdocker://")) {
    print("MCDocker initiated from uri schema");
  }

  VanillaClient(ClientManifest("test", 8, "s", "", "")).downloadJava();

  if (!PlatformUtils().mcdockerDir.existsSync() || arguments.isEmpty) {
    await PlatformUtils().mcdockerDir.create();
    print("\nWelcome to ${bold(blue("MCDocker"))}!");
    print(
        "${bold(blue("MCDocker"))} is a lightweight and simple launcher for ${bold(green("Minecraft"))}.");
    print("\nTo get started, run the ${bold(yellow("help"))} command.\n");
    return;
  }
  CommandManager().run(arguments);
}
