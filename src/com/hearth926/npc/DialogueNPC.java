package com.hearth926.npc;

import com.hearth926.localization.TextManager;
import com.hearth926.player.Player;
import com.hearth926.worldLayer.Grid;

import java.util.ArrayList;
import java.util.List;

public class DialogueNPC extends BaseNPC {
    private final String npcId;
    private final List<String> dialogueSequenceIds; // sequence of dialogue IDs
    private final List<String> dialogueLines = new ArrayList<>();
    private int dialogueIndex = 0;

    /**
     * @param row NPC row
     * @param col NPC column
     * @param grid Grid
     * @param type NPC type
     * @param npcId The ID used in the JSON (npc_id)
     * @param dialogueSequenceIds List of dialogue IDs to play in order
     * @param interactionRange interaction range
     */
    public DialogueNPC(int row, int col, Grid grid, NPCType type,
                       String npcId, List<String> dialogueSequenceIds, double interactionRange) {
        super(row, col, grid, type.getColor(), type, interactionRange);
        this.npcId = npcId;
        this.dialogueSequenceIds = dialogueSequenceIds;
        preloadDialogue();
    }

    // Preload dialogue lines from TextManager using npcId and dialogue IDs
    private void preloadDialogue() {
        dialogueLines.clear();
        if (npcId != null && dialogueSequenceIds != null) {
            for (String dialogueId : dialogueSequenceIds) {
                String line = TextManager.getInstance().getNPCDialogue(npcId, dialogueId);
                dialogueLines.add(line);
            }
        }
    }

    // Get the current line
    public String getCurrentLine() {
        if (isDialogueFinished()) return "...";
        return dialogueLines.get(dialogueIndex);
    }

    // Advance to next line
    public void advanceDialogue() {
        if (!isDialogueFinished()) dialogueIndex++;
    }

    // Returns true if dialogue is finished
    public boolean isDialogueFinished() {
        return dialogueLines.isEmpty() || dialogueIndex >= dialogueLines.size();
    }

    public void resetDialogue() {
        dialogueIndex = 0;
    }

    @Override
    protected void defaultInteract(Player player) {
        System.out.println("Dialogue NPC says: " + getCurrentLine());
        advanceDialogue();
    }
}
