package com.hearth926.game;

import com.hearth926.worldLayer.Grid;
import com.hearth926.interaction.InteractionManager;
import com.hearth926.npc.TestNPC;
import com.hearth926.player.Player;
import com.hearth926.player.enums.Direction;
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

        root.getChildren().addAll(grid.getTilesAsNodes());
        root.getChildren().add(player.getNode());
        root.getChildren().add(npc.getNode());

        scene = new Scene(root, width, height);

        // Register NPC for interactions
        interactionManager.register(npc);
        interactionManager.startAutoFocus();

        setupInput(interactionManager);
    }

    private void setupInput(InteractionManager interactionManager) {
        scene.setOnKeyPressed(event -> {
//            keysPressed.add(event.getCode());
//            handleMovement();

            // Interaction
            if (event.getCode() == KeyCode.E) {
                interactionManager.interact();
                return;
            }

            // Movement
            keysPressed.add(event.getCode());
            handleMovement();
        });

        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
    }

    private void handleMovement() {
        boolean up = keysPressed.contains(KeyCode.W) || keysPressed.contains(KeyCode.UP);
        boolean down = keysPressed.contains(KeyCode.S) || keysPressed.contains(KeyCode.DOWN);
        boolean left = keysPressed.contains(KeyCode.A) || keysPressed.contains(KeyCode.LEFT);
        boolean right = keysPressed.contains(KeyCode.D) || keysPressed.contains(KeyCode.RIGHT);

        // Diagonal movement (priority)
        if (up && left) player.move(Direction.UP_LEFT);
        else if (up && right) player.move(Direction.UP_RIGHT);
        else if (down && left) player.move(Direction.DOWN_LEFT);
        else if (down && right) player.move(Direction.DOWN_RIGHT);

        // Cardinal movement
        else if (up) player.move(Direction.UP);
        else if (down) player.move(Direction.DOWN);
        else if (left) player.move(Direction.LEFT);
        else if (right) player.move(Direction.RIGHT);
    }

    public Scene getScene() {
        return scene;
    }
}
