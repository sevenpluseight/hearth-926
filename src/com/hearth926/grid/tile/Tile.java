package com.hearth926.grid.tile;

import com.hearth926.grid.tile.enums.TileType;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Polygon;
import javafx.scene.Node;

public class Tile {
    private final int row;
    private final int col;
    private final Polygon polygon;
    private TileType type;

    public Tile(int row, int col, double x, double y, double tileWidth, double tileHeight, TileType type) {
        this.row = row;
        this.col = col;
        this.type = type;

        polygon = new Polygon(
                x, y,
                x + tileWidth / 2, y + tileHeight / 2,
                x, y + tileHeight,
                x - tileWidth / 2, y + tileHeight / 2
        );

        setType(type);
        polygon.setStroke(Color.DARKGRAY);
    }

    public void setType(TileType type) {
        this.type = type;
        if(type.getImage() != null){
            polygon.setFill(new ImagePattern(type.getImage()));
        } else {
            polygon.setFill(type.getColor());
        }
    }

    public Node getNode() {
        return polygon;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}
