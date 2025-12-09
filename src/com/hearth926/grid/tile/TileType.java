package com.hearth926.grid.tile;

import javafx.scene.paint.Color;
import javafx.scene.image.Image;

import java.util.Objects;

public enum TileType {
    GRASS(Color.LIGHTGREEN, "/images/tiles/grass.png"),
    DIRT(Color.SADDLEBROWN, "/images/tiles/dirt.png"),
    WATER(Color.DEEPSKYBLUE, "/images/tiles/water.png");

    private final Color color;
    private final Image image;

    TileType(Color color, String resourcePath) {
        this.color = color;
        // Load from resources folder
        Image img;
        try {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream(resourcePath)));
        } catch (Exception e) {
            System.out.println("Failed to load image: " + resourcePath);
            img = null;
        }
        this.image = img;
    }

    public Color getColor() { return color; }
    public Image getImage() { return image; }
}
