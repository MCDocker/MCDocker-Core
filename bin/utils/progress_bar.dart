import 'dart:async';
import 'dart:io';

class Progress {
  double percentage = 0;
  final String status;
  final String progressCharacter;
  late Timer timer;

  Progress(this.status, {this.progressCharacter = '#'});

  set add(double percentage) => this.percentage += percentage;
  set subtract(double percentage) => this.percentage -= percentage;
  set percent(double percentage) => this.percentage = percentage;

  Future<Timer> start() {
    Completer<Timer> completer = Completer();

    String spaces = " " *
        ((stdout.terminalColumns / 2).ceil() -
            status.length -
            percentage.toStringAsFixed(2).length -
            7);

    timer = Timer.periodic(Duration(seconds: 1), (Timer t) {
      percentage = percentage.clamp(0, 100);
      int count = (percentage * spaces.length) ~/ 100;
      String bar = progressCharacter * count;

      stdout.write(
          "\r$status [${bar + spaces.substring(bar.length)}] ${percentage.toStringAsFixed(2)}% ${t.tick}s");
      if (percentage >= 100.00) {
        percentage = 100.00;
        t.cancel();
        completer.complete(t);
        stdout.write("\n");
      }
    });

    return completer.future;
  }

  stop() => timer.cancel();

  reset() {
    percentage = 0;
    timer.cancel();
  }
}
