package com.hearth926.control;

import com.hearth926.interaction.Interactable;
import com.hearth926.player.Player;
import javafx.animation.AnimationTimer;
import javafx.scene.effect.DropShadow;
import javafx.scene.paint.Color;

import java.util.Comparator;
import java.util.List;

public class InteractionFocusController {
    private Interactable focused;
    private final Player player;
    private final List<Interactable> interactables;

    private static final DropShadow GLOW = new DropShadow(18, Color.LIGHTYELLOW);
    private double pulse = 0;
    private boolean increasing = true;

    // Construct controller with a player and interactable list
    public InteractionFocusController(Player player, List<Interactable> interactables) {
        this.player = player;
        this.interactables = interactables;
    }

    // Start automatic focus update with a pulsing glow
    public void start() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateFocus();
                animateGlow();
            }
        }.start();
    }

    // Updates which object should be focused based on proximity
    public void updateFocus() {
        Interactable nearest = interactables.stream()
                .filter(i -> i.isInRange(player))
                .min(Comparator.comparingDouble(this::distance))
                .orElse(null);

        if (nearest == focused) return;

        clearFocus();
        setFocus(nearest);

        focused = nearest;
    }

    // Apply highlight and callback to a new focused object
    private void setFocus(Interactable obj) {
        if (obj == null || !obj.shouldHighlight()) return;

        obj.getNode().setEffect(GLOW);
        obj.onFocus();
    }

    // Remove highlight and trigger blur callback
    private void clearFocus() {
        if (focused == null || !focused.shouldHighlight()) return;

        focused.getNode().setEffect(null);
        focused.onBlur();
    }

    // Animate glow for pulsing effect
    private void animateGlow() {
        if (focused == null) return;

        if (increasing) {
            pulse += 0.5;
            if (pulse > 20) increasing = false;
        } else {
            pulse -= 0.5;
            if (pulse < 5) increasing = true;
        }

        GLOW.setRadius(pulse);
    }

    // Measure distance from object to player
    private double distance(Interactable obj) {
        double dx = obj.getNode().getTranslateX() - player.getNode().getTranslateX();
        double dy = obj.getNode().getTranslateY() - player.getNode().getTranslateY();
        return Math.hypot(dx, dy);
    }
}
