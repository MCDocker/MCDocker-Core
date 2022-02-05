import 'dart:async';
import 'dart:convert';
import 'dart:io';

import 'package:http/http.dart';

import '../../mcdocker.dart';
import '../../platform/platform_utils.dart';
import '../../server/http_wrapper.dart';
import '../account.dart';
import '../authentication.dart';

class MicrosoftAuth extends Authentication {
  @override
  Future<Account> authenticate({StreamConsumer<String>? status}) async {
    Completer<Account> completer = Completer();

    Future<HttpServer> server = HTTPWrapper().start(6167);
    PlatformUtils().wrapper.openUrl(
        "https://login.live.com/oauth20_authorize.srf?client_id=ad15277d-c44c-43c8-89ee-67b44dac5a84&response_type=code&scope=XboxLive.signin%20offline_access");

    void sendResponse(HttpRequest req, String error) {
      status?.addStream(Stream.value(error));
      status?.close();

      req.response.statusCode = 400;
      req.response.headers.contentType = ContentType.json;
      req.response.write(jsonEncode({"error": error}));
      req.response.close();

      completer.completeError(jsonEncode({'error': error}));
      return;
    }

    server.then((server) => {
          server.forEach((req) {
            var params = req.uri.queryParameters;

            if (!params.containsKey("code"))
              sendResponse(req, 'Code not found');

            var codeToToken = _codeToToken(params['code'].toString());
            // if (codeToToken == null) sendResponse(req, )
            var accessToken = jsonDecode(codeToToken)['additionalContent']
                    ['access_token']
                .toString();
          })
        });

    return completer.future;
  }

  _codeToToken(String code) async =>
      await Client().post(Uri.parse("${apiUrl}auth/microsoft?code=$code"));
}
