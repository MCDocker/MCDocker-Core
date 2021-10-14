package io.mcdocker.launcher.fx.components.settings;

import io.mcdocker.launcher.utils.StringUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

public class SettingsGroup extends VBox {

    private final List<SettingsEntry> entries;


    public SettingsGroup(String name, List<SettingsEntry> entries) {
        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/settings/SettingsGroup.fxml"));
        loader.setController(this);
        loader.setRoot(this);
        this.entries = entries;

        try {
            loader.load();

            Label title = (Label) lookup("#groupTitle");
            title.setText(StringUtils.uppercaseFirstLetterOfString(name));

            for(SettingsEntry entry : entries) {
                System.out.println(entry.getName());
                getChildren().add(entry);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public Label getTitle() {return title;}
//    public List<SettingsEntry> getEntries() {return entries;}
}
