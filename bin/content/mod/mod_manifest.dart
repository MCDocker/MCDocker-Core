class ModManifest {
  String _name;
  String _id;
  String _version;
  final String _type;

  ModManifest(this._name, this._id, this._version, this._type);

  get name => _name;

  set setName(String s) => _name = s;

  get id => _id;

  set setId(String s) => _id = s;

  get version => _version;

  set setVersion(String s) => _version = s;

  get type => _type;
}
