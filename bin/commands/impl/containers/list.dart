import 'package:args/command_runner.dart';

import '../../../container/container.dart';

class ListCommand extends Command {
  @override
  String get description =>
      "Lists all containers that are installed on your system";

  @override
  String get name => "list";

  @override
  void run() {
    print(Container.getContainers());
  }
}
