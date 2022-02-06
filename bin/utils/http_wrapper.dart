import 'dart:async';
import 'dart:io';

import 'progress_bar.dart';

class HTTPUtils {
  late HttpServer _server;

  start(int port) {
    Future<HttpServer> future =
        HttpServer.bind(InternetAddress.loopbackIPv4, port);
    future.catchError((err) => print("An error has occurred. $err"));
    future.then((s) => {print("Listening on port ${s.port}"), _server = s});
    return future;
  }

  stop({bool force = false}) => _server.close(force: force);
  get server => _server;

  static Future<dynamic> downloadFile(String url, File dest,
      {bool progressBar = false}) async {
    final request = await HttpClient().getUrl(Uri.parse(url));
    final response = await request.close();
    final file = dest.openWrite();

    Progress progress = Progress("Downloading Java");
    Future<Timer> timer = progress.start();

    int downloaded = 0;

    return response.listen((data) {
      downloaded += data.length;
      progress.percentage = (downloaded / response.contentLength) * 100;
      file.add(data);
    }).asFuture();

    // return response.pipe(dest.openWrite());
  }
}
