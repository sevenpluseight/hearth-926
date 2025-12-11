package com.hearth926.npc;

import javafx.scene.paint.Color;

public enum NPCType {
    SIDE_CHARACTER(Color.DARKBLUE),
    SHOPKEEPER(Color.GOLD),
    GUIDE(Color.DARKGREEN);

    private final Color color;

    NPCType(Color color) {
        this.color = color;
    }

    public Color getColor() {
        return color;
    }
}
