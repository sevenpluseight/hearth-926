package com.hearth926.game;

import com.hearth926.control.CameraController;
import com.hearth926.control.InputManager;
import com.hearth926.worldLayer.Grid;
import com.hearth926.interaction.InteractionManager;
import com.hearth926.npc.TestNPC;
import com.hearth926.player.Player;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameScene {
    private final Scene scene;
    private final Player player;

    public GameScene(double width, double height) {
        Grid grid = new Grid(10, 10, width, height, 48, 24, 25);
        player = new Player(0, 0, grid);
        InteractionManager interactionManager = new InteractionManager(player);

        TestNPC npc = new TestNPC(4, 5, grid, "test_npc_dialogue", 20);
        grid.registerNPC(npc);

        Pane world = new Pane();
        world.getChildren().addAll(grid.getTilesAsNodes());
        world.getChildren().add(player.getNode());
        world.getChildren().add(npc.getNode());

        Pane root = new Pane(world);

        scene = new Scene(root, width, height);

        // Register NPC for interaction
        interactionManager.register(npc);
        interactionManager.startAutoFocus();

        // Input manager handles movement and interaction
        InputManager inputManager = new InputManager(player, interactionManager, scene);

        // Camera setup
        CameraController camera = new CameraController(world, player.getNode(), width, height);
        camera.setSmoothing(0.10);

        // Main game loop
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                inputManager.update();
                camera.update();
            }
        }.start();

        root.setFocusTraversable(true);
        Platform.runLater(root::requestFocus);
    }

    public Scene getScene() {
        return scene;
    }
}
