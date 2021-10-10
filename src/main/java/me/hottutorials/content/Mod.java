package me.hottutorials.content;

public class Mod {

    private final String name;
    private final String description;
    private final ClientType type;
    private final String link;
    private final String author;
    private final String version;
    private final String icon;

    public Mod(String name, String description, ClientType type, String link, String author, String version, String icon) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.link = link;
        this.author = author;
        this.version = version;
        this.icon = icon;
    }

    public String getName() { return name; }
    public String getIcon() { return icon; }
    public String getVersion() { return version; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getLink() { return link; }
    public ClientType getType() { return type; }

    @Override
    public String toString() {
        return "Mod{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", version='" + version + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }

}
