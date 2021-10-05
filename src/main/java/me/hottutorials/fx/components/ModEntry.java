package me.hottutorials.fx.components;

import javafx.beans.NamedArg;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import me.hottutorials.content.Mod;

import java.io.IOException;

public class ModEntry extends Button {

    private final Mod mod;

    public ModEntry(Mod mod) {
        this.mod = mod;
        FXMLLoader entry = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/ModEntry.fxml"));
        entry.setController(this);
        entry.setRoot(this);

        try {
            entry.load();

            Label title = (Label) entry.getNamespace().get("modTitle");
            Label description = (Label) entry.getNamespace().get("modDesc");
//            ImageView icon = (ImageView) entry.getNamespace().get("modIconView");

            title.setText(mod.getName());
            description.setText(mod.getDescription());
//            if(mod.getIcon() != null) icon.setImage(new Image(mod.getIcon()));
//            else icon.setImage(new Image(getClass().getClassLoader().getResource("img/pack.png").getPath()));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mod getMod() {
        return mod;
    }
}
