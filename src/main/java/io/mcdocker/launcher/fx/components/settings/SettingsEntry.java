package io.mcdocker.launcher.fx.components.settings;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public class SettingsEntry extends AnchorPane {

    private final String name;
    private final String description;
    private final Object value;

    public SettingsEntry(String name, String description, Object value) {
        this.name = name;
        this.description = description;
        this.value = value;

        FXMLLoader loader = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/settings/SettingsEntry.fxml"));
        loader.setController(this);
        loader.setRoot(this);

        try {
            loader.load();

            Label labelName = (Label) lookup("#settingName");
            Label labelDescription = (Label) lookup("#settingDescription");

            StringBuilder builder = new StringBuilder();
            for(String str : name.split("_"))
                builder.append(str).append(" ");

            labelName.setText(builder.toString());
            labelDescription.setText(description);

            // TODO: Implment option changer

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Object getValue() {
        return value;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
