package com.hearth926.game;

import com.hearth926.control.CameraController;
import com.hearth926.control.InputManager;
import com.hearth926.npc.TestNPC;
import com.hearth926.worldLayer.Grid;
import com.hearth926.interaction.InteractionManager;
import com.hearth926.player.Player;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class GameScene {
    private final Scene scene;
    private final Player player;

    public GameScene(double width, double height) {
        Grid grid = new Grid(10, 10, width, height, 48, 24, 25);
        player = new Player(0, 0, grid);

        // World layer
        Pane world = new Pane();
        world.getChildren().addAll(grid.getTilesAsNodes());
        world.getChildren().add(player.getNode());

        // UI layer
        Pane uiLayer = new Pane();

        // Root layer
        Pane root = new Pane(world, uiLayer);
        scene = new Scene(root, width, height);

        // Test NPC
        TestNPC npc1 = new TestNPC(4, 5, grid, "test_npc_dialogue_1", 20);
        grid.registerNPC(npc1);
        world.getChildren().add(npc1.getNode());

        TestNPC npc2 = new TestNPC(7, 8, grid, "test_npc_dialogue_2", 20);
        grid.registerNPC(npc2);
        world.getChildren().add(npc2.getNode());

        InteractionManager interactionManager = new InteractionManager(player, uiLayer);
        interactionManager.register(npc1);
        interactionManager.register(npc2);

        // Input
        InputManager inputManager = new InputManager(player, interactionManager, scene);

        // Camera
        CameraController camera = new CameraController(world, player.getNode(), width, height);
        camera.setSmoothing(0.10);

        new AnimationTimer() {
            @Override
            public void handle(long now) {
                inputManager.update();
                camera.update();
            }
        }.start();

        // Focus handling
        root.setFocusTraversable(true);
        Platform.runLater(root::requestFocus);
    }

    public Scene getScene() {
        return scene;
    }
}
