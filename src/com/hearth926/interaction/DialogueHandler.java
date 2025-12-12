package com.hearth926.interaction;

import com.hearth926.npc.DialogueNPC;
import com.hearth926.ui.DialogueBox;

public class DialogueHandler {
    private DialogueNPC activeNPC = null;
    private final DialogueBox dialogueBox;

    public DialogueHandler(DialogueBox box) {
        this.dialogueBox = box;
    }

    // Returns true if the dialogue box is currently visible
    public boolean isDialogueVisible() {
        return dialogueBox.isShowing();
    }

    // Returns true if a conversation with an NPC is active
    public boolean isActive() {
        return activeNPC != null;
    }

    // Returns the current active NPC
    public DialogueNPC getActiveNPC() {
        return activeNPC;
    }

    /**
     * Start a dialogue with the given NPC or continue the active one if the same
     * If switching to a new NPC, ends the previous conversation first
     */
    public void startOrContinue(DialogueNPC npc) {
        if (activeNPC != null && activeNPC != npc) {
            endDialogue(); // reset previous conversation
        }
        if (!isActive()) startDialogue(npc);
        else continueDialogue();
    }

    // Start a new dialogue from the first line of the given NPC
    public void startDialogue(DialogueNPC npc) {
        if (activeNPC != null && activeNPC != npc) endDialogue();

        activeNPC = npc;
        activeNPC.resetDialogue();

        if (activeNPC.isDialogueFinished()) {
            endDialogue();
            return;
        }

        showLine(activeNPC.getCurrentLine());
    }

    // Continue the dialogue with the active NPC (called on pressing E)
    public void continueDialogue() {
        if (activeNPC == null) return;

        activeNPC.advanceDialogue();

        if (activeNPC.isDialogueFinished()) {
            endDialogue();
            return;
        }

        showLine(activeNPC.getCurrentLine());
    }

    // Ends the current dialogue and hides the UI
    public void endDialogue() {
        if (activeNPC != null) activeNPC.resetDialogue();
        activeNPC = null;
        dialogueBox.hideDialogue();
    }

    /**
     * Forcefully end dialogue if the player is out of range
     * @param isInRange true if the player is still in the interaction range
     */
    public void forceEndIfOutOfRange(boolean isInRange) {
        if (!isInRange && activeNPC != null) {
            endDialogue();
        }
    }

    // Helper to show a line on the dialogue box
    private void showLine(String text) {
        if (text == null || text.isEmpty()) text = "...";
        dialogueBox.showDialogue(text, 30);
    }
}
