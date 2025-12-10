package com.hearth926.worldLayer;

import com.hearth926.worldLayer.tile.Tile3D;
import com.hearth926.worldLayer.tile.TileType;
import javafx.scene.Node;

import java.util.ArrayList;
import java.util.List;

public class Grid {
    private final int rows;
    private final int cols;
    private final double tileWidth;
    private final double tileHeight;
    private final double sceneWidth;
    private final double sceneHeight;
    private final double tileDepth;

    private final double xOffset;
    private final double yOffset;

    private final List<Tile3D> tiles = new ArrayList<>();

    public Grid(int rows, int cols, double sceneWidth, double sceneHeight,
                double tileWidth, double tileHeight, double tileDepth) {
        this.rows = rows;
        this.cols = cols;
        this.sceneWidth = sceneWidth;
        this.sceneHeight = sceneHeight;
        this.tileWidth = tileWidth;
        this.tileHeight = tileHeight;
        this.tileDepth = tileDepth;

        // Center the grid in the scene
        this.xOffset = sceneWidth / 2;
        this.yOffset = sceneHeight / 2 - (rows + cols) * tileHeight / 4;

        createGrid();
    }

    private void createGrid() {
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                TileType type = TileType.GRASS; // default tile

                Tile3D tile = new Tile3D(
                        row,
                        col,
                        tileWidth,
                        tileHeight,
                        tileDepth,
                        type
                );

                // Apply offsets
                tile.getNode().setTranslateX(tile.getNode().getTranslateX() + xOffset);
                tile.getNode().setTranslateY(tile.getNode().getTranslateY() + yOffset);

                tiles.add(tile);
            }
        }
    }

    public List<Node> getTilesAsNodes() {
        List<Node> nodes = new ArrayList<>();
        for (Tile3D tile : tiles) {
            nodes.add(tile.getNode());
        }
        return nodes;
    }

    // Getters
    public double getTileWidth() { return tileWidth; }
    public double getTileHeight() { return tileHeight; }
    public int getRows() { return rows; }
    public int getCols() { return cols; }
    public double getSceneWidth() { return sceneWidth; }
    public double getSceneHeight() { return sceneHeight; }
    public double getTileDepth() { return tileDepth; }
    public double getXOffset() { return xOffset; }
    public double getYOffset() { return yOffset; }
}
