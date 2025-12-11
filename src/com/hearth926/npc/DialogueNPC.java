package com.hearth926.npc;

import com.hearth926.localization.TextManager;
import com.hearth926.player.Player;
import com.hearth926.worldLayer.Grid;

import java.util.List;

public class DialogueNPC extends BaseNPC {
    private final String dialogueKey;
    private List<String> dialogueLines;
    private int dialogueIndex = 0;

    public DialogueNPC(int row, int col, Grid grid, NPCType type, String dialogueKey, double interactionRange) {
        super(row, col, grid, type.getColor(), type, interactionRange);
        this.dialogueKey = dialogueKey;
        preloadDialogue();
    }

    private void preloadDialogue() {
        if (dialogueKey != null) {
            dialogueLines = TextManager.getInstance().getTextList(dialogueKey);
        }
    }

    // Returns the current dialogue line (or "..." if none)
    public String getDialogue() {
        if (dialogueLines == null || dialogueLines.isEmpty()) return "...";
        if (dialogueIndex >= dialogueLines.size()) dialogueIndex = dialogueLines.size() - 1;
        return dialogueLines.get(dialogueIndex);
    }

    /**
     * Advances the dialogue
     * @return true if the dialogue has finished (after the last line)
     */
    public boolean advanceDialogue() {
        if (dialogueLines == null || dialogueLines.isEmpty()) return true;

        dialogueIndex++;
        if (dialogueIndex >= dialogueLines.size()) {
            dialogueIndex = 0;
            return true;
        }
        return false; // not finished
    }

    public void resetDialogue() {
        dialogueIndex = 0;
    }

    @Override
    protected void defaultInteract(Player player) {
        System.out.println("Dialogue NPC says: " + getDialogue());
        advanceDialogue();
    }
}
