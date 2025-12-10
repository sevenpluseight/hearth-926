package com.hearth926.control;

import com.hearth926.player.Player;
import com.hearth926.player.enums.Direction;
import com.hearth926.interaction.InteractionManager;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;

import java.util.HashSet;
import java.util.Set;

public class InputManager {
    private final Player player;
    private final InteractionManager interactionManager;
    private final Set<KeyCode> keysPressed = new HashSet<>();

    public InputManager(Player player, InteractionManager interactionManager, Scene scene) {
        this.player = player;
        this.interactionManager = interactionManager;

        registerInput(scene);

        Platform.runLater(() -> scene.getRoot().requestFocus());
    }

    private void registerInput(Scene scene) {
        scene.setOnKeyPressed(event -> {
            KeyCode code = event.getCode();

            // Interaction
            if (code == KeyCode.E) {
                interactionManager.interact();
                return;
            }

            // Movement
            keysPressed.add(code);
        });

        scene.setOnKeyReleased(event -> keysPressed.remove(event.getCode()));
    }

    // Call every frame from AnimationTimer
    public void update() {
        if (player.isMoving()) return;

        Direction nextMove = getNextDirection();
        if (nextMove != null) {
            player.move(nextMove);
        }
    }

    private Direction getNextDirection() {
        boolean up = keysPressed.contains(KeyCode.W) || keysPressed.contains(KeyCode.UP);
        boolean down = keysPressed.contains(KeyCode.S) || keysPressed.contains(KeyCode.DOWN);
        boolean left = keysPressed.contains(KeyCode.A) || keysPressed.contains(KeyCode.LEFT);
        boolean right = keysPressed.contains(KeyCode.D) || keysPressed.contains(KeyCode.RIGHT);

        // Diagonal priority
        if (up && left) return Direction.UP_LEFT;
        if (up && right) return Direction.UP_RIGHT;
        if (down && left) return Direction.DOWN_LEFT;
        if (down && right) return Direction.DOWN_RIGHT;

        // Cardinal
        if (up) return Direction.UP;
        if (down) return Direction.DOWN;
        if (left) return Direction.LEFT;
        if (right) return Direction.RIGHT;

        return null; // no movement
    }
}
