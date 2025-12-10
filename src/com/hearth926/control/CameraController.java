package com.hearth926.control;

import javafx.scene.Node;
import javafx.scene.layout.Pane;

public class CameraController {
    private final Pane world;
    private final Node target;
    private final double sceneWidth;
    private final double sceneHeight;

    // Smoothness (0 = rigid, 1 = instant snap)
    private double smoothing = 0.12;

    public CameraController(Pane world, Node target, double sceneWidth, double sceneHeight) {
        this.world = world;
        this.target = target;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
    }

    // Call every frame
    public void update() {
        double targetX = target.getTranslateX();
        double targetY = target.getTranslateY();

        double centerX = sceneWidth / 2;
        double centerY = sceneHeight / 2;

        double desiredX = centerX - targetX;
        double desiredY = centerY - targetY;

        world.setTranslateX(lerp(world.getTranslateX(), desiredX, smoothing));
        world.setTranslateY(lerp(world.getTranslateY(), desiredY, smoothing));
    }

    // Linear interpolation for smooth camera movement
    private double lerp(double start, double end, double amount) {
        return start + (end - start) * amount;
    }

    // Adjust how "floaty" the camera is
    public void setSmoothing(double smoothing) {
        this.smoothing = smoothing;
    }
}
