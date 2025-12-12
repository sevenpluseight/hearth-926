package com.hearth926.interaction;

import com.hearth926.npc.DialogueNPC;
import com.hearth926.player.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * Registry for all interactable objects in the scene
 * Responsible for storing interactables and answering queries
 * such as "which DialogueNPC is the closest and in range"
 */
public class InteractableRegistry {
    private final Player player;
    private final List<Interactable> interactables = new ArrayList<>();

    public InteractableRegistry(Player player) {
        this.player = player;
    }

    public void register(Interactable obj) {
        if (!interactables.contains(obj)) interactables.add(obj);
    }

    public List<Interactable> getInteractables() {
        return interactables;
    }

    /**
     * Finds the closest DialogueNPC to the player that is in range
     * Returns null if none found
     */
    public DialogueNPC getClosestDialogueNPCInRange() {
        return interactables.stream()
                .filter(obj -> obj instanceof DialogueNPC && obj.isInRange(player))
                .map(obj -> (DialogueNPC) obj)
                .min((a, b) -> Double.compare(distance(a), distance(b)))
                .orElse(null);
    }

    private double distance(DialogueNPC npc) {
        double dx = npc.getNode().getTranslateX() - player.getNode().getTranslateX();
        double dy = npc.getNode().getTranslateY() - player.getNode().getTranslateY();
        return Math.hypot(dx, dy);
    }
}
