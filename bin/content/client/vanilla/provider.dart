import 'dart:convert';

import 'package:http/http.dart';

import '../client.dart' as C;
import '../client_manifest.dart';
import '../client_details.dart';
import '../client_provider.dart';
import 'client.dart';
import 'manifest.dart';

class VanillaProvider implements ClientProvider<VanillaClient> {
  @override
  Future<List<ClientDetails<VanillaClient>>> getClients() {
    return Future(() async {
      List<ClientDetails<VanillaClient>> clients = [];
      Response response = await get(Uri.parse(
          "https://launchermeta.mojang.com/mc/game/version_manifest.json"));
      List<dynamic> versionsArray = jsonDecode(response.body)['versions'];

      for (var client in versionsArray) {
        clients.add(ClientDetails(client['id'], Future((() async {
          String dataUrl = client['url'];
          Map<String, dynamic> data =
              jsonDecode((await get(Uri.parse(dataUrl))).body);
          return VanillaClient(
              VanillaManifest(
                  dataUrl: dataUrl,
                  name: data['id'],
                  javaVersion: data.containsKey('javaVersion')
                      ? data['javaVersion']['majorVersion']
                      : 8,
                  mainClass: data['mainClass'],
                  startupArguments: appendArguments(parseArguments(data))),
              data);
        }))));
      }

      return clients;
    });
  }

  @override
  Future<C.Client<VanillaManifest>> getClient(String name) {
    return Future(() async {
      try {
        return (await (await getClients())
            .firstWhere((el) => el.name.toLowerCase() == name.toLowerCase())
            .future);
      } catch (e) {
        return Future.error("Not found");
      }
    }); // Future
  }

  static String parseArguments(Map<String, dynamic> data) {
    if (data.containsKey("arguments")) {
      List<dynamic> argumentsArray = data["arguments"]["game"];
      List<String> builder = [];
      for (int i = 0; i < argumentsArray.length; i++) {
        if (argumentsArray.elementAt(i).runtimeType != String) continue;
        builder.add(argumentsArray.elementAt(i));
        if (i != argumentsArray.length - 1) builder.add(" ");
      }
      return builder.toString();
    } else if (data.containsKey("minecraftArguments")) {
      return data["minecraftArguments"];
    }
    return r"--username ${auth_player_name} "
        r"--version ${version_name} "
        r"--gameDir ${game_directory} "
        r"--assetsDir ${assets_root} "
        r"--assetIndex ${assets_index_name} "
        r"--uuid ${auth_uuid} "
        r"--accessToken ${auth_access_token} "
        r"--userProperties ${user_properties} "
        r"--userType ${user_type}";
  }

  static String appendArguments(String arguments) {
    return r"-XX:-UseAdaptiveSizePolicy "
            r"-XX:-OmitStackTraceInFastThrow "
            r"-Dminecraft.launcher.brand=mc-docker "
            r"-Dminecraft.launcher.version=1 "
            r"-Djava.library.path=${natives} "
            r"-Xms${min_memory}M "
            r"-Xmx${max_memory}M "
            r"-cp ${libraries} "
            r"${main_class} " +
        arguments;
  }
}
