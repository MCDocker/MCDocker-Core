package me.hottutorials.fx;

import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class FXUtils {

    public static <T> T load(String name) throws IOException {
        return FXMLLoader.load(FXUtils.class.getClassLoader().getResource("fxml/MainScene.fxml"));
    }

}
