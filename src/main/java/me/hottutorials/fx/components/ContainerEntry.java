package me.hottutorials.fx.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;

import java.io.IOException;

public class ContainerEntry extends Button {

    public ContainerEntry() {
        FXMLLoader entry = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/ContainerEntry.fxml"));
        entry.setController(this);
        entry.setRoot(this);

        try {
            entry.load();

            Label title = (Label) entry.getNamespace().get("contTitle");
            Label description = (Label) entry.getNamespace().get("contDesc");
            ImageView icon = (ImageView) entry.getNamespace().get("contIconView");

//            title.setText(mod.getName());
//            description.setText(mod.getDescription());
//
//            if(mod.getIcon() != null) {
//                Image image = new Image(mod.getIcon());
//                icon.setImage(image);
//            } else {
//                Image image = new Image(String.valueOf(getClass().getClassLoader().getResource("img/pack.png")));
//                icon.setImage(image);
//            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
