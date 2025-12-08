package com.hearth926;

import com.hearth926.game.GameScene;
import javafx.application.Application;
import javafx.geometry.Rectangle2D;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {

        Rectangle2D screen = Screen.getPrimary().getVisualBounds();

        double width = screen.getWidth();
        double height = screen.getHeight();

        GameScene gameScene = new GameScene(width, height);

        stage.setTitle("Hearth:926");
        stage.setScene(gameScene.getScene());
        stage.setX(screen.getMinX());
        stage.setY(screen.getMinY());
        stage.setWidth(width);
        stage.setHeight(height);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
