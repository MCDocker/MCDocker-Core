package me.hottutorials.content.modrinth;

import java.util.List;

public class ModrinthSerializer {

    public String id;
    public String name;
    public String description;
    public String published;
    public String updated;
    public String license;
    public String downloads;
    public String icon;
    public String body;

    public List<ModrinthCategories> categories;
    public List<String> versions;

}
