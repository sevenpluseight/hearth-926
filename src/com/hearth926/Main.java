package com.hearth926;

import com.hearth926.game.GameScene;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) {
        double width = 800;
        double height = 800;

        GameScene gameScene = new GameScene(width, height);

        stage.setTitle("Hearth:926");
        stage.setScene(gameScene.getScene());
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
