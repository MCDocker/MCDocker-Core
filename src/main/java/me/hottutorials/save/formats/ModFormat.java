package me.hottutorials.save.formats;

public class ModFormat {

    String name;
    String version;
    String link;

    public ModFormat(String name, String version, String link) {
        this.name = name;
        this.version = version;
        this.link = link;
    }

    public String getCombinedString() {
        String newName = name.replaceAll(" ", "-").replaceAll(";", "");
        String newVersion = version.replaceAll(" ", "-").replaceAll(";", "");

        String newLink = link != null
                ? link.replaceAll("https", "").replaceAll("http", "").replaceAll("://", "")
                : "custom";

        return newName + ";" + newVersion + ";" + newLink;
    }

    public String getNameFromString(String str) {
        String[] strings = str.split(";");
        if(strings.length != 3) return null;
        return strings[0];
    }

    public String getVersionFromString(String str) {
        String[] strings = str.split(";");
        if(strings.length != 3) return null;
        return strings[0];
    }

    public String getLinkFromString(String str) {
        String[] strings = str.split(";");
        if(strings.length != 3) return null;
        return strings[0];
    }

}
