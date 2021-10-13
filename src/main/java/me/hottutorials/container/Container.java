package me.hottutorials.container;

import me.grison.jtoml.impl.Toml;
import me.hottutorials.save.Save;
import me.hottutorials.utils.OSUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Container {

    private String name;
    private final Save save;

    private final Toml toml = new Toml();

    public Container(String name, Save save) {
        this.name = name;
        this.save = save;
    }

    private void createFolders() {
        final File containersPath = new File(OSUtils.getUserData() + "containers");
        if(!containersPath.exists()) containersPath.mkdir();

        final File containerFolder = new File(containersPath + "/" + name);
        if(!containerFolder.exists()) containerFolder.mkdir();
    }

    public File getDockerFile() {
        return new File(OSUtils.getUserData() + "containers" + "/" + name + "/.mcdocker");
    }

    public void createContainer() throws IOException {
        createFolders();

        if(!getDockerFile().exists()) getDockerFile().createNewFile();

        FileWriter writer = new FileWriter(getDockerFile());
        writer.write(Toml.serialize("meta", save));
        writer.flush();
        writer.close();
    }

    public Save getDockerContent() {
        System.out.println(toml.parseFile(getDockerFile()).get("meta"));
        Save format = toml.parseFile(getDockerFile()).getAs("meta.type", Save.class);
        return format;
    }



}
