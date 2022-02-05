import 'dart:io';

class HTTPWrapper {
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
}
