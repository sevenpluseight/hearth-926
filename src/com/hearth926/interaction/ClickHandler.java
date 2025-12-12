package com.hearth926.interaction;

import com.hearth926.npc.DialogueNPC;
import com.hearth926.player.Player;

/**
 * Responsible for attaching mouse click listeners to interactables.
 * On click, it delegates to DialogueHandler (for DialogueNPC) or calls interact() otherwise
 */
public class ClickHandler {
    private final Player player;
    private final DialogueHandler dialogueHandler;

    public ClickHandler(Player player, DialogueHandler dialogueHandler) {
        this.player = player;
        this.dialogueHandler = dialogueHandler;
    }

    public void attachClickable(Interactable obj) {
        // Only attach if clickable
        if (!obj.isClickable()) return;

        obj.getNode().setOnMouseClicked(e -> {
            if (!obj.isInRange(player)) return;

            if (obj instanceof DialogueNPC dialogueNPC) {
                dialogueHandler.startOrContinue(dialogueNPC);
            } else {
                obj.interact(player);
            }
        });
    }
}