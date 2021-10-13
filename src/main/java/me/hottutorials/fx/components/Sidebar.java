package me.hottutorials.fx.components;

import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import me.hottutorials.fx.ContainersScene;
import me.hottutorials.fx.HomeScene;
import me.hottutorials.fx.MainScene;
import me.hottutorials.fx.SettingsScene;

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

            containers.setOnAction((e) -> setActivePanel(containers, ContainersScene.class));
            home.setOnAction((e) -> setActivePanel(home, HomeScene.class));
            settings.setOnAction((e) -> setActivePanel(settings, SettingsScene.class));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActivePanel(Button button, Class clazz) {
        for(Region panel : MainScene.getPanelsList()) {
            panel.setVisible(false);
//            button.getStyleClass().clear();
//            button.getStyleClass().add("sidebar-button");
            if(panel.getClass() == clazz) {
                panel.setVisible(true);
//                button.getStyleClass().add("active-sidebar-button");
            }
        }
    }

}
