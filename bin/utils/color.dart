// Taken from https://gist.github.com/fnky/458719343aabd01cfb17a3a4f7296797

// Colors
String red(String text) => "\x1B[31m$text\x1B[0m";
String black(String text) => "\x1B[30m$text\x1B[0m";
String green(String text) => "\x1B[32m$text\x1B[0m";
String yellow(String text) => "\x1B[33m$text\x1B[0m";
String blue(String text) => "\x1B[34m$text\x1B[0m";
String magenta(String text) => "\x1B[35m$text\x1B[0m";
String cyan(String text) => "\x1B[36m$text\x1B[0m";
String white(String text) => "\x1B[37m$text\x1B[0m";

// Formats
String bold(String text) => "\x1B[1m$text\x1B[0m";
String underline(String text) => "\x1B[4m$text\x1B[0m";
String reverse(String text) => "\x1B[7m$text\x1B[0m";
