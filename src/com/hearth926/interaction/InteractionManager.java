package com.hearth926.interaction;

import com.hearth926.control.InteractionFocusController;
import com.hearth926.npc.BaseNPC;
import com.hearth926.npc.DialogueNPC;
import com.hearth926.player.Player;
import com.hearth926.ui.DialogueBox;
import javafx.animation.AnimationTimer;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;

public class InteractionManager {
    private final Player player;
    private final List<Interactable> interactables = new ArrayList<>();
    private final InteractionFocusController focusController;
    private final DialogueBox dialogueBox;

    private DialogueNPC activeDialogueNPC = null;

    public InteractionManager(Player player, Pane uiLayer) {
        this.player = player;
        this.focusController = new InteractionFocusController(player, interactables);

        dialogueBox = new DialogueBox(300, 80);
        dialogueBox.setTranslateX(20);
        dialogueBox.setTranslateY(20);
        uiLayer.getChildren().add(dialogueBox);
    }

    // Called when player presses E or clicks an NPC
    public void interact() {
        DialogueNPC closestNPC = getClosestDialogueNPCInRange();
        if (closestNPC == null) return;

        // Reset previous NPC if switching
        if (activeDialogueNPC != null && activeDialogueNPC != closestNPC) {
            activeDialogueNPC.resetDialogue();
        }

        activeDialogueNPC = closestNPC;

        // Show current line
        showDialogue(activeDialogueNPC.getDialogue());

        // Advance dialogue; if finished, end conversation
        if (activeDialogueNPC.advanceDialogue()) {
            activeDialogueNPC = null;
            hideDialogue();
        }
    }

    public void register(Interactable obj) {
        if (!interactables.contains(obj)) {
            interactables.add(obj);
            attachClickHandler(obj);
        }
    }

    private void attachClickHandler(Interactable obj) {
        if (!obj.isClickable()) return;

        obj.getNode().setOnMouseClicked(e -> {
            if (obj.isInRange(player)) {
                obj.interact(player);

                if (obj instanceof DialogueNPC dialogueNPC) {
                    // Reset previous NPC if switching
                    if (activeDialogueNPC != null && activeDialogueNPC != dialogueNPC) {
                        activeDialogueNPC.resetDialogue();
                    }

                    activeDialogueNPC = dialogueNPC;
                    showDialogue(activeDialogueNPC.getDialogue());

                    // Advance dialogue; if finished, end conversation
                    if (activeDialogueNPC.advanceDialogue()) {
                        activeDialogueNPC = null;
                        hideDialogue();
                    }
                }
            }
        });
    }

    // Returns the closest DialogueNPC in range or null
    public DialogueNPC getClosestDialogueNPCInRange() {
        return interactables.stream()
                .filter(obj -> obj instanceof DialogueNPC && obj.isInRange(player))
                .map(obj -> (DialogueNPC) obj)
                .min((a, b) -> Double.compare(distance(a), distance(b)))
                .orElse(null);
    }

    // Returns the closest BaseNPC in range or null
    public BaseNPC getClosestNPCInRange() {
        return interactables.stream()
                .filter(BaseNPC.class::isInstance)
                .map(BaseNPC.class::cast)
                .min((a, b) -> Double.compare(distance(a), distance(b)))
                .orElse(null);
    }

    private double distance(BaseNPC npc) {
        double dx = npc.getNode().getTranslateX() - player.getNode().getTranslateX();
        double dy = npc.getNode().getTranslateY() - player.getNode().getTranslateY();
        return Math.hypot(dx, dy);
    }

    public void startAutoFocus() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                focusController.updateFocus();

                // Reset dialogue if player walks out of range
                if (activeDialogueNPC != null && !activeDialogueNPC.isInRange(player)) {
                    activeDialogueNPC.resetDialogue();
                    activeDialogueNPC = null;
                    hideDialogue();
                }
            }
        }.start();
    }

    private void showDialogue(String text) {
        if (text == null || text.isEmpty()) text = "...";
        dialogueBox.showDialogue(text, 30);
    }

    public void hideDialogue() {
        dialogueBox.hideDialogue();
        activeDialogueNPC = null;
    }

    // Returns true if the player is currently in dialogue
    public boolean isDialogueActive() {
        return activeDialogueNPC != null;
    }
}
