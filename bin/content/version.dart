class Version {
  final int minor;
  final int patch;
  final int major;

  Version({
    this.major = 1,
    this.minor = 8,
    this.patch = 9,
  });

  @override
  String toString() {
    return "$major.$minor.$patch";
  }
}
