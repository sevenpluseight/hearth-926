package com.hearth926.game;

import com.hearth926.control.CameraController;
import com.hearth926.worldLayer.Grid;
import com.hearth926.interaction.InteractionManager;
import com.hearth926.npc.TestNPC;
import com.hearth926.player.Player;
import com.hearth926.player.enums.Direction;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.util.HashSet;
import java.util.Set;

public class GameScene {
    private final Scene scene;
    private final Player player;
    private final Set<KeyCode> keysPressed = new HashSet<>();

    public GameScene(double width, double height) {
        Grid grid = new Grid(10, 10, width, height, 48, 24, 25);
        player = new Player(0, 0, grid);
        InteractionManager interactionManager = new InteractionManager(player);

        TestNPC npc = new TestNPC(4, 5, grid, "test_npc_dialogue", 20);

        Pane root = new Pane();
        Pane world = new Pane();

        root.getChildren().add(world);
        world.getChildren().addAll(grid.getTilesAsNodes());
        world.getChildren().add(player.getNode());
        world.getChildren().add(npc.getNode());

        scene = new Scene(root, width, height);

        // Register NPC for interactions
        interactionManager.register(npc);
        interactionManager.startAutoFocus();

        setupInput(interactionManager);

        // Camera
        CameraController camera = new CameraController(world, player.getNode(), width, height);
        camera.setSmoothing(0.10);
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                camera.update();
            }
        }.start();

        // Movement loop
        new javafx.animation.AnimationTimer() {
            @Override
            public void handle(long now) {
                handleMovement();
            }
        }.start();

        // Ensure the Scene has focus to receive key events
        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    private void setupInput(InteractionManager interactionManager) {
        scene.setOnKeyPressed(event -> {
            // Interaction
            if (event.getCode() == KeyCode.E) {
                interactionManager.interact();
                return;
            }

            // Movement
            keysPressed.add(event.getCode());
        });

        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
    }

    private void handleMovement() {
        if (player.isMoving()) return;

        boolean up = keysPressed.contains(KeyCode.W) || keysPressed.contains(KeyCode.UP);
        boolean down = keysPressed.contains(KeyCode.S) || keysPressed.contains(KeyCode.DOWN);
        boolean left = keysPressed.contains(KeyCode.A) || keysPressed.contains(KeyCode.LEFT);
        boolean right = keysPressed.contains(KeyCode.D) || keysPressed.contains(KeyCode.RIGHT);

        Direction nextMove = null;

        if (up && left) nextMove = Direction.UP_LEFT;
        else if (up && right) nextMove = Direction.UP_RIGHT;
        else if (down && left) nextMove = Direction.DOWN_LEFT;
        else if (down && right) nextMove = Direction.DOWN_RIGHT;
        else if (up) nextMove = Direction.UP;
        else if (down) nextMove = Direction.DOWN;
        else if (left) nextMove = Direction.LEFT;
        else if (right) nextMove = Direction.RIGHT;

        if (nextMove != null) player.move(nextMove);
    }

    public Scene getScene() {
        return scene;
    }
}
