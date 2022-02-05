import 'dart:io';

import 'commands/manager.dart';
import 'platform/platform_utils.dart';
import 'utils/color.dart';

String apiUrl = "http://20.113.69.92:8080/api/";

Future<void> main(List<String> arguments) async {
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
