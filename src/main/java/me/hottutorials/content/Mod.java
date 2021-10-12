package me.hottutorials.content;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public abstract class Mod {

    private final String name;
    private final String description;
    private final ClientType type;
    private final String link;
    private final String author;
    private final String icon;

    public Mod(String name, String description, ClientType type, String link, String author, String icon) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.link = link;
        this.author = author;
        this.icon = icon;
    }

    public String getName() { return name; }
    public String getIcon() { return icon; }
    public String getDescription() { return description; }
    public String getAuthor() { return author; }
    public String getLink() { return link; }
    public ClientType getType() { return type; }
    public abstract CompletableFuture<Set<ModVersion>> getVersions();

    @Override
    public String toString() {
        return "Mod{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + type +
                ", link='" + link + '\'' +
                ", author='" + author + '\'' +
                ", icon='" + icon + '\'' +
                '}';
    }
}
