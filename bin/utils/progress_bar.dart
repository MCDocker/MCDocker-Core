import 'dart:async';
import 'dart:io';

class Progress {
  double percentage = 0;
  final String status;
  final String progressCharacter;
  late Timer timer;

  Progress(this.status, {this.progressCharacter = '#'});

  Future start() {
    Completer completer = Completer();

    String spaces = " " *
        ((stdout.terminalColumns / 2).ceil() -
            status.length -
            percentage.toStringAsFixed(2).length -
            7);

    print(percentage);

    Timer.periodic(Duration(seconds: 1), (Timer t) async {
      percentage = percentage.clamp(0, 100);
      String bar = progressCharacter * ((percentage * spaces.length) ~/ 100);
      stdout.write(
          "\r$status [${bar + spaces.substring(bar.length)}] ${percentage.toStringAsFixed(2)}% ${t.tick}s");

      if (percentage >= 100.00) {
        stdout.write("\n");
        t.cancel();
        return completer.complete();
      }
    });

    return completer.future;
  }
}
