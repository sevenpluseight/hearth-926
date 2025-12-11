package com.hearth926.npc;

import com.hearth926.player.Player;
import com.hearth926.worldLayer.Grid;

public class TestNPC extends DialogueNPC {
    public TestNPC(int row, int col, Grid grid, String dialogueKey, double interactionRange) {
        super(row, col, grid, NPCType.SIDE_CHARACTER, dialogueKey, interactionRange);
    }

    @Override
    protected void defaultInteract(Player player) {
        super.defaultInteract(player);
    }
}
