class NumberUtils {
  static int? parseInt(String s) {
    try {
      return int.parse(s);
    } catch (e) {
      return null;
    }
  }

  static double? parseDouble(String s) {
    try {
      return double.parse(s);
    } catch (e) {
      return null;
    }
  }
}
