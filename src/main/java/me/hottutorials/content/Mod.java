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

    public static class ModProperties {

        private static final ModProperties instance = new ModProperties();

        public static ModProperties getBuilder() { return instance; }

        private String name;
        private String description;
        private ClientType type;
        private String link;
        private String author = null;
        private String version = null;
        private String icon = null;

        public Mod getMod() { return new Mod(name, description, type, link, author, version, icon); }

        public String getName() { return name; }
        public String getIcon() { return icon; }
        public String getVersion() { return version; }
        public String getDescription() { return description; }
        public String getAuthor() { return author; }
        public String getLink() { return link; }
        public ClientType getType() { return type; }

        public ModProperties setDescription(String description) { this.description = description; return this; }
        public ModProperties setLink(String link) { this.link = link; return this; }
        public ModProperties setName(String name) { this.name = name; return this;}
        public ModProperties setType(ClientType type) { this.type = type; return this; }
        public ModProperties setAuthor(String author) { this.author = author; return this; }
        public ModProperties setIcon(String icon) { this.icon = icon; return this; }
        public ModProperties setVersion(String version) { this.version = version; return this; }

    }

}
