package com.hearth926.interaction;

import com.hearth926.player.Player;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;

public class InteractionRange {
    private static final double RANGE_MULTIPLIER = 0.8; // player needs to be 80% of the original range

    // Checks if a node is within interaction distance of the player
    public static boolean inRange(Player player, Node node, double range) {
        double npcCenterX = node.getTranslateX();
        double npcCenterY = node.getTranslateY();

        if (node instanceof Rectangle rect) {
            npcCenterX += rect.getWidth() / 2;
            npcCenterY += rect.getHeight() / 2;
        }

        double dx = player.getCenterX() - npcCenterX;
        double dy = player.getCenterY() - npcCenterY;
        double adjustedRange = range * RANGE_MULTIPLIER;

        return Math.sqrt(dx * dx + dy * dy) <= adjustedRange;
    }
}
