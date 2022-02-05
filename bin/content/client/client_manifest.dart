class ClientManifest {
  String _name;
  String get name => _name;
  set name(String value) => _name = value;

  int _javaVersion;
  get javaVersion => _javaVersion;
  set javaVersion(value) => _javaVersion = value;

  String _mainClass;
  String get mainClass => _mainClass;
  set mainClass(String value) => _mainClass = value;

  String _startupArguments;
  get startupArguments => _startupArguments;
  set startupArguments(value) => _startupArguments = value;

  final String _type;
  get type => _type;

  ClientManifest(this._name, this._javaVersion, this._mainClass,
      this._startupArguments, this._type);
}
