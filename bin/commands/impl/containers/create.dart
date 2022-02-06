import 'package:args/command_runner.dart';

import '../../../container/container.dart';
import '../../../container/docker.dart';

class CreateCommand extends Command {
  @override
  String get description => 'Creates a container';
  @override
  String get name => 'create';

  @override
  void run() {
    Container container = Container(DockerFile());
    container.dockerfile.client;
  }
}
