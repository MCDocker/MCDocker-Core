package me.hottutorials.utils;

import javafx.application.Platform;
import javafx.scene.Node;

import java.util.function.Consumer;

public class FXUtils {

    public static <T extends Node> void editNode(T node, Consumer<T> consumer) {
        editFx(() -> consumer.accept(node));
    }

    public static void editFx(Runnable runnable) {
        Platform.runLater(runnable);
    }

}
