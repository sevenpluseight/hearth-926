package com.hearth926.interaction;

import com.hearth926.player.Player;
import javafx.animation.AnimationTimer;

import java.util.ArrayList;
import java.util.List;

public class InteractionManager {
    private final Player player;
    private final List<Interactable> interactables = new ArrayList<>();
    private final InteractionFocusController focusController;

    public InteractionManager(Player player) {
        this.player = player;
        this.focusController = new InteractionFocusController(player, interactables);
    }

    // Public interaction API
    public void interact() {
        interactNearest();
    }

    // Register an interactable
    public void register(Interactable obj) {
        if (!interactables.contains(obj)) {
            interactables.add(obj);
            attachClickHandler(obj);
        }
    }

    // Mouse interaction
    private void attachClickHandler(Interactable obj) {
        if (!obj.isClickable()) return;

        obj.getNode().setOnMouseClicked(e -> {
            if (obj.isInRange(player)) {
                obj.interact(player);
            }
        });
    }

    // Core interaction engine
    private void interactNearest() {
        interactables.stream()
                .filter(obj -> obj.isInRange(player))
                .min((a, b) -> Double.compare(distance(a), distance(b)))
                .ifPresent(obj -> obj.interact(player));
    }

    // Start an automatic focus system
    public void startAutoFocus() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                focusController.updateFocus();
            }
        }.start();
    }

    // Distance helper
    private double distance(Interactable obj) {
        double dx = obj.getNode().getTranslateX() - player.getNode().getTranslateX();
        double dy = obj.getNode().getTranslateY() - player.getNode().getTranslateY();
        return Math.hypot(dx, dy);
    }
}
