import 'dart:async';

import 'account.dart';
import 'authentication.dart';

abstract class EmailPasswordAuth implements Authentication {
  Future<Account> authenticateEmailPassword(String email, String password,
      {StreamConsumer<String> status});
}
