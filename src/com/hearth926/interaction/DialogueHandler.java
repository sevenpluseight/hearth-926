package com.hearth926.interaction;

import com.hearth926.npc.DialogueNPC;
import com.hearth926.ui.DialogueBox;

public class DialogueHandler {
    private DialogueNPC active = null;
    private final DialogueBox box;

    public DialogueHandler(DialogueBox box) {
        this.box = box;
    }

    // True if the UI box is visible (dialogue currently shown)
    public boolean isDialogueVisible() {
        return box.isShowing();
    }

    // True if currently have an active NPC (conversation in progress)
    public boolean isActive() {
        return active != null;
    }

    public DialogueNPC getActiveNPC() {
        return active;
    }

    // Start a dialogue with npc OR continue the same npc
    public void startOrContinue(DialogueNPC npc) {
        if (active != null && active != npc) {
            // switching NPCs: reset old conversation
            endDialogue();
        }
        if (!isActive()) startDialogue(npc);
        else continueDialogue();
    }

    // Begin a new dialogue from the first line
    public void startDialogue(DialogueNPC npc) {
        if (active != null && active != npc) endDialogue();

        active = npc;
        active.resetDialogue();

        if (active.isDialogueFinished()) {
            // nothing meaningful to show
            endDialogue();
            return;
        }

        show(active.getCurrentLine());
    }

    // Called when player presses E while a conversation is active
    public void continueDialogue() {
        if (active == null) return;

        active.advanceDialogue();

        if (active.isDialogueFinished()) {
            // conversation ended — hide UI and clear active
            endDialogue();
            return;
        }

        show(active.getCurrentLine());
    }

    // Forcefully end and clear the current dialogue
    public void endDialogue() {
        if (active != null) active.resetDialogue();
        active = null;
        box.hideDialogue();
    }

    // Called by InteractionManager each frame to force-end if player left range
    public void forceEndIfOutOfRange(boolean isInRange) {
        if (!isInRange && active != null) {
            endDialogue();
        }
    }

    private void show(String text) {
        if (text == null || text.isEmpty()) text = "...";
        box.showDialogue(text, 30);
    }
}