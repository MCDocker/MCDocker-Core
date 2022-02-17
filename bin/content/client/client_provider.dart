import 'client.dart';
import 'client_details.dart';

abstract class ClientProvider<T extends Client> {
  Future<List<ClientDetails<T>>> getClients();
  Future<Client?> getClient(String name);
}
