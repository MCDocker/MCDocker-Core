package me.hottutorials.fx.components;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.hottutorials.fx.ContainersScene;
import me.hottutorials.fx.HomeScene;
import me.hottutorials.fx.MainScene;
import me.hottutorials.fx.SettingsScene;
import org.kordamp.ikonli.Ikon;
import org.kordamp.ikonli.bootstrapicons.BootstrapIcons;
import org.kordamp.ikonli.bootstrapicons.BootstrapIconsIkonProvider;

import java.io.IOException;

public class Sidebar extends VBox {

    public Sidebar() {
        FXMLLoader sidebar = new FXMLLoader(getClass().getClassLoader().getResource("fxml/components/Sidebar.fxml"));
        sidebar.setController(this);
        sidebar.setRoot(this);

        try {
            sidebar.load();

            Button home = (Button) lookup("#homeSidebarButton");
            Button containers = (Button) lookup("#containersSidebarButton");
            Button settings = (Button) lookup("#settingsSidebarButton");

            containers.setOnAction((e) -> setActivePanel(ContainersScene.class));
            home.setOnAction((e) -> setActivePanel(HomeScene.class));
            settings.setOnAction((e) -> setActivePanel(SettingsScene.class));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActivePanel(Class clazz) {
        for(Region panel : MainScene.getPanelsList()) {
            panel.setVisible(false);
            if(panel.getClass() == clazz) panel.setVisible(true);
        }
    }

}
