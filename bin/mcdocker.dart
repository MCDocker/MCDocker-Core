import 'auth/account.dart';
import 'auth/authentication.dart';
import 'commands/manager.dart';
import 'container/container.dart';
import 'container/docker.dart';
import 'content/client/client_manifest.dart';
import 'content/client/vanilla/manifest.dart';
import 'content/client/vanilla/provider.dart';
import 'platform/platform_utils.dart';
import 'utils/color.dart';

String apiUrl = "http://20.113.69.92:8080/api/";

Future<void> main(List<String> arguments) async {
  if (arguments.isNotEmpty && arguments[0].startsWith("mcdocker://")) {
    print("MCDocker initiated from uri schema");
  }

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

// void testLaunch() async {
//   Container container = Container(DockerFile());
//   VanillaManifest vanillaClient =
//       (await VanillaProvider().getClient("1.8.9")).manifest;
//   container.dockerfile.client = vanillaClient.toJson();
//   container.dockerfile.name = 'Test Container';
//   container.saveSync();

//   Account account = Account("Player420", "0", "0", []);
//   ClientManifest manifest = container.dockerfile.
// }
