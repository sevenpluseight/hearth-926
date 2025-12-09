package com.hearth926.grid.tile;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

public class Tile3D {
    private final Group block;
    private final Polygon top;
    private final Polygon left;
    private final Polygon right;
    private TileType type;

    public Tile3D(double x, double y, double tileWidth, double tileHeight, TileType type, double height) {
        this.type = type;
        block = new Group();

        // Top face
        top = new Polygon(
                x, y,
                x + tileWidth / 2, y + tileHeight / 2,
                x, y + tileHeight,
                x - tileWidth / 2, y + tileHeight / 2
        );
        top.setStroke(Color.DARKGRAY);

        // Left face
        left = new Polygon(
                x - tileWidth / 2, y + tileHeight / 2,
                x, y + tileHeight,
                x, y + tileHeight + height,
                x - tileWidth / 2, y + tileHeight / 2 + height
        );

        // Right face
        right = new Polygon(
                x + tileWidth / 2, y + tileHeight / 2,
                x, y + tileHeight,
                x, y + tileHeight + height,
                x + tileWidth / 2, y + tileHeight / 2 + height
        );

        applyType(type);
        block.getChildren().addAll(left, right, top);
    }

    private void applyType(TileType type) {
        if (type.getImage() != null) {
            top.setFill(new ImagePattern(type.getImage()));
        } else {
            top.setFill(type.getColor());
        }

        left.setFill(getSideColor(type, 0.7));
        right.setFill(getSideColor(type, 0.5));
    }

    private Color getSideColor(TileType type, double factor) {
        return type.getColor().deriveColor(0, 1, factor, 1);
    }

    public Node getNode() {
        return block;
    }

    public void setType(TileType type) {
        this.type = type;
        applyType(type);
    }
}
