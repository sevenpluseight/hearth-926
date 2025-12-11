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
        if (isDialogueActive()) {
            processInteraction(activeDialogueNPC);
        } else {
            DialogueNPC closestNPC = getClosestDialogueNPCInRange();
            if (closestNPC != null) {
                processInteraction(closestNPC);
            }
        }
    }

    private void processInteraction(DialogueNPC npc) {
        if (activeDialogueNPC != npc) {
            if (activeDialogueNPC != null) {
                activeDialogueNPC.resetDialogue();
            }
            activeDialogueNPC = npc;
            activeDialogueNPC.resetDialogue(); // Start from the beginning

            // Show the first line, but only if the NPC has dialogue
            if (activeDialogueNPC.isDialogueFinished()) {
                hideDialogue(); // Closes the box immediately
                activeDialogueNPC = null;
            } else {
                showDialogue(activeDialogueNPC.getCurrentLine());
            }
        } else { // continuing with the same NPC
            activeDialogueNPC.advanceDialogue();

            if (activeDialogueNPC.isDialogueFinished()) {
                hideDialogue();
                activeDialogueNPC.resetDialogue();
                activeDialogueNPC = null;
            } else {
                showDialogue(activeDialogueNPC.getCurrentLine());
            }
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
            if (!obj.isInRange(player)) return;
            if (obj instanceof DialogueNPC dialogueNPC) {
                processInteraction(dialogueNPC);
            } else {
                obj.interact(player);
            }
        });
    }

    // Returns the closest DialogueNPC in range
    public DialogueNPC getClosestDialogueNPCInRange() {
        return interactables.stream()
                .filter(obj -> obj instanceof DialogueNPC && obj.isInRange(player))
                .map(obj -> (DialogueNPC) obj)
                .min((a, b) -> Double.compare(distance(a), distance(b)))
                .orElse(null);
    }

    // Returns the closest BaseNPC in range
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
    }

    public boolean isDialogueActive() {
        return activeDialogueNPC != null;
    }
}
