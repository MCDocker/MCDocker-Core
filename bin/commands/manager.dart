import 'dart:io';

import 'package:args/args.dart';
import 'package:args/command_runner.dart';

import 'impl/containers/list.dart';

class CommandManager {
  final List<Command> commands = [];

  CommandManager() {
    register(ListCommand());
  }

  void register(Command cmd) => commands.add(cmd);
  void registerAll(List<Command> cmds) => commands.addAll(cmds);

  Future<void> run(List<String> arguments) async {
    final runner = CommandRunner("mcdocker", "> MCDocker");

    for (var cmd in commands) {
      runner.addCommand(cmd);
    }

    return await runner.run(arguments).catchError((err) {
      print(err);
      exitCode = 1;
    });
  }
}
