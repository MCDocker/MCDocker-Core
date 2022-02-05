import 'dart:async';
import 'account.dart';

abstract class Authentication {
  Future<Account> authenticate({StreamConsumer<String>? status});
}
