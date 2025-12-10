package com.hearth926.npc;

import com.hearth926.worldLayer.Grid;
import com.hearth926.player.Player;
import javafx.scene.paint.Color;

public class TestNPC extends BaseNPC {
    public TestNPC(int row, int col, Grid grid, String dialogueKey, double interactionRange) {
        super(row, col, grid, Color.BLUE, NPCType.SIDE_CHARACTER, dialogueKey, interactionRange);

        setOnInteract(player -> {
            System.out.println("Custom interaction with TestNPC at (" + row + ", " + col + ")");
            if (hasQuest()) {
                System.out.println("This NPC has a quest available!");
            }
            startDialogue();
        });
    }

    @Override
    public void interact(Player player) {
        super.interact(player);
    }
}