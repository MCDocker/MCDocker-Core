class ClientManifest {
  String name;
  int javaVersion;
  String mainClass;
  String startupArguments;
  final String type;

  ClientManifest(this.name, this.javaVersion, this.mainClass,
      this.startupArguments, this.type);

  ClientManifest.fromJson(Map<String, dynamic> json)
      : name = json['name'],
        javaVersion = json['javaVersion'],
        mainClass = json['mainClass'],
        startupArguments = json['startupArguments'],
        type = json['type'];

  Map<String, dynamic> toJson() => {
        'name': name,
        'javaVersion': javaVersion,
        'mainClass': mainClass,
        'startupArguments': startupArguments,
        'type': type
      };
}
