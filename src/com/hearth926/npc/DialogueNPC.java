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

    // Preload dialogue from TextManager
    private void preloadDialogue() {
        if (dialogueKey != null) {
            dialogueLines = TextManager.getInstance().getTextList(dialogueKey);
        }
    }

    // Returns the current line
    public String getCurrentLine() {
        if (isDialogueFinished()) return "...";
        return dialogueLines.get(dialogueIndex);
    }

    // Advance to the next line, if any
    public void advanceDialogue() {
        if (dialogueLines != null && !isDialogueFinished()) {
            dialogueIndex++;
        }
    }

    // Returns true if the last line is currently displayed
    public boolean isDialogueFinished() {
        return dialogueLines == null || dialogueLines.isEmpty() || dialogueIndex >= dialogueLines.size();
    }

    // Reset dialogue for next interaction
    public void resetDialogue() {
        dialogueIndex = 0;
    }

    @Override
    protected void defaultInteract(Player player) {
        System.out.println("Dialogue NPC says: " + getCurrentLine());
        advanceDialogue();
    }
}
