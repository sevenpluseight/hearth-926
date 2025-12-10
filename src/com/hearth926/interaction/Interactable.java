package com.hearth926.interaction;

import com.hearth926.player.Player;
import javafx.scene.Node;

public interface Interactable {
    void interact(Player player);
    boolean isInRange(Player player);
    Node getNode();

    // Optional: override if an object should allow click
    default boolean isClickable() {
        return true;
    }

    // Optional: override if an object should not highlight
    default boolean shouldHighlight() {
        return true;
    }

    // Optional: visual feedback hooks
    default void onFocus() {};
    default void onBlur() {};
}
