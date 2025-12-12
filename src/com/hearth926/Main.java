package com.hearth926;

import com.hearth926.game.GameScene;
import com.hearth926.game.TitleScene;
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

        TitleScene titleScene = new TitleScene(width, height, () -> {
            GameScene gameScene = new GameScene(width, height);
            stage.setScene(gameScene.getScene());
        });

        stage.setTitle("Hearth: 926");
//        stage.setScene(gameScene.getScene());
        stage.setX(screen.getMinX());
        stage.setY(screen.getMinY());
        stage.setWidth(width);
        stage.setHeight(height);
        stage.setScene(titleScene.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
