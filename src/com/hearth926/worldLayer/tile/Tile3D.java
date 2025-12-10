package com.hearth926.worldLayer.tile;

import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;

public class Tile3D {
    private final Group block;
    private final Polygon top, left, right;
    private final int row, col;
    private TileType type;

    public Tile3D(int row, int col, double tileWidth, double tileHeight, double height, TileType type) {
        this.row = row;
        this.col = col;
        this.type = type;

        block = new Group();

        double x = (col - row) * tileWidth / 2;
        double y = (col + row) * tileHeight / 2;

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

        left.setFill(type.getColor().deriveColor(0, 1, 0.7, 1));
        right.setFill(type.getColor().deriveColor(0, 1, 0.5, 1));
    }

//    private Color getSideColor(TileType type, double factor) {
//        return type.getColor().deriveColor(0, 1, factor, 1);
//    }

    public Node getNode() {
        return block;
    }

    public void setType(TileType type) {
        this.type = type;
        applyType(type);
    }

    // Tile coordination for logic
    public int getRow() { return row; }
    public int getCol() { return col; }
}
