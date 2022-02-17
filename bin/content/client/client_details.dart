import 'client.dart';

class ClientDetails<T extends Client> {
  final String name;
  final Future<T> future;

  ClientDetails(this.name, this.future);
}
