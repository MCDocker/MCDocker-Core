package me.hottutorials.fx;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainScene extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXUtils.load("fxml/MainScene.fxml");
        Scene scene = new Scene(root);

        stage.setWidth(900.0);
        stage.setHeight(600.0);
        stage.setTitle("MCDocker");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
