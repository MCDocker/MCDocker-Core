class Account {
  final String _username;
  final String _uuid;
  final String _accessToken;
  final List<String> _skins;

  Account(this._username, this._uuid, this._accessToken, this._skins);

  get username => _username;
  get uuid => _uuid;
  get accessToken => _accessToken;
  get skins => _skins;
}
