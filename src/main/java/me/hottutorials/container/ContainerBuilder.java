package me.hottutorials.container;

import me.hottutorials.save.Save;

public class ContainerBuilder {

    private String name;
    private Save save;

    public Container build() {
        return new Container(name, save);
    }

    public ContainerBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public ContainerBuilder setSave(Save save) {
        this.save = save;
        return this;
    }

}
