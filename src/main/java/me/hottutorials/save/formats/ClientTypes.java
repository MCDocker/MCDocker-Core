package me.hottutorials.save.formats;

public enum ClientTypes {
    VANILLA("Vanilla"),
    FORGE("Forge"),
    FABRIC("Fabric"),
    CUSTOM("Custom");

    String name;

    ClientTypes(String name) { this.name = name; }
}
