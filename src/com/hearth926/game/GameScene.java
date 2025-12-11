package com.hearth926.game;

import com.hearth926.control.CameraController;
import com.hearth926.control.InputManager;
import com.hearth926.npc.DialogueNPC;
import com.hearth926.worldLayer.Grid;
import com.hearth926.interaction.InteractionManager;
import com.hearth926.npc.BaseNPC;
import com.hearth926.npc.TestNPC;
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

        // UI layer for dialogues
        Pane uiLayer = new Pane();

        // Root pane contains both world and UI
        Pane root = new Pane(world, uiLayer);
        scene = new Scene(root, width, height);

        // Setup test NPC
        TestNPC npc = new TestNPC(4, 5, grid, "test_npc_dialogue", 20);
        grid.registerNPC(npc);
        world.getChildren().add(npc.getNode());

        // Interaction manager handles player interactions
        InteractionManager interactionManager = new InteractionManager(player, uiLayer);
        interactionManager.register(npc);
        interactionManager.startAutoFocus();

        // Input manager handles movement + interactions
        InputManager inputManager = new InputManager(player, interactionManager, scene);

        // Camera setup
        CameraController camera = new CameraController(world, player.getNode(), width, height);
        camera.setSmoothing(0.10);

        // Track the last NPC in range to reset dialogue when the player moves away
        BaseNPC[] lastNPC = {null};

        // Main game loop
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                inputManager.update();
                camera.update();

                // Check if player moved away from active NPC
                BaseNPC closestNPC = interactionManager.getClosestNPCInRange();
                if (lastNPC[0] != null && lastNPC[0] != closestNPC) {
                    // Player left the previous NPC
                    if (lastNPC[0] instanceof DialogueNPC dialogueNPC) {
                        dialogueNPC.resetDialogue();
                    }
                    interactionManager.hideDialogue();
                }
                lastNPC[0] = closestNPC;
            }
        }.start();

        root.setFocusTraversable(true);
        Platform.runLater(root::requestFocus);
    }

    public Scene getScene() {
        return scene;
    }
}
