package com.hearth926.npc;

import com.hearth926.player.Player;
import com.hearth926.worldLayer.Grid;

import java.util.List;

public class TestNPC extends DialogueNPC {

    /**
     * @param row NPC's grid row
     * @param col NPC's grid column
     * @param grid World grid
     * @param npcId The ID used in the NPC JSON file
     * @param dialogueSequenceIds List of dialogue IDs to play in order
     * @param interactionRange How close the player must be to interact
     */
    public TestNPC(int row, int col, Grid grid, String npcId, List<String> dialogueSequenceIds, double interactionRange) {
        super(row, col, grid, NPCType.SIDE_CHARACTER, npcId, dialogueSequenceIds, interactionRange);
    }

    @Override
    protected void defaultInteract(Player player) {
        super.defaultInteract(player);
    }
}
