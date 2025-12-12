package com.hearth926.interaction;

import com.hearth926.control.InteractionFocusController;
import com.hearth926.npc.DialogueNPC;
import com.hearth926.player.Player;
import com.hearth926.ui.DialogueBox;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

public class InteractionManager {
    private final Player player;
    private final InteractableRegistry registry;
    private final DialogueHandler dialogueHandler;
    private final ClickHandler clickHandler;
    private final InteractionFocusController focusController;

    public InteractionManager(Player player, Pane uiLayer) {
        this.player = player;

        DialogueBox box = new DialogueBox(300, 80);
        box.setTranslateX(20);
        box.setTranslateY(20);
        uiLayer.getChildren().add(box);

        // subsystems
        this.registry = new InteractableRegistry(player);
        this.dialogueHandler = new DialogueHandler(box);
        this.clickHandler = new ClickHandler(player, dialogueHandler);
        this.focusController = new InteractionFocusController(player, registry.getInteractables());

        startAutoFocus();
    }

    public void register(Interactable obj) {
        registry.register(obj);
        clickHandler.attachClickable(obj);
    }

    /**
     * Called when player presses E
     * If dialogue active -> continue; otherwise find closest NPC and start
     */
    public void interact() {
        if (dialogueHandler.isActive()) {
            dialogueHandler.continueDialogue();
            return;
        }

        DialogueNPC npc = registry.getClosestDialogueNPCInRange();
        if (npc != null) dialogueHandler.startDialogue(npc);
    }

    private void startAutoFocus() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                focusController.updateFocus();

                // if a dialogue is active, ensure it ends when the player leaves range
                if (dialogueHandler.isActive()) {
                    boolean inRange = dialogueHandler.getActiveNPC().isInRange(player);
                    dialogueHandler.forceEndIfOutOfRange(inRange);
                }
            }
        }.start();
    }

    // Expose whether dialogue is active (used by InputManager to block movement)
    public boolean isDialogueActive() {
        return dialogueHandler.isActive();
    }
}
