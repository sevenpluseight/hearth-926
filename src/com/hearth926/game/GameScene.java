package com.hearth926.game;

import com.hearth926.grid.Grid;
import com.hearth926.player.Player;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameScene {
    private final Scene scene;
    private final Player player;

    public GameScene(double width, double height) {
        Pane root = new Pane();
        Grid grid = new Grid(10, 10, width, height, 48, 24);
        player = new Player(0, 0, grid);

        root.getChildren().addAll(grid.getTilesAsNodes());
        root.getChildren().add(player.getNode());

        scene = new Scene(root, width, height);

        setupInput();
    }

    private void setupInput() {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case W, UP -> player.moveUp();
                case A, LEFT -> player.moveLeft();
                case S, DOWN -> player.moveDown();
                case D, RIGHT -> player.moveRight();
            }
        });
    }

    public Scene getScene() {
        return scene;
    }
}
