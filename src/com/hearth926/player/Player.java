package com.hearth926.player;

import com.hearth926.grid.Grid;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Player {
    private int row;
    private int col;
    private final Rectangle node;
    private final Grid grid;
    private final double size = 15;

    public Player(int row, int col, Grid grid) {
        this.row = row;
        this.col = col;
        this.grid = grid;
        this.node = new Rectangle(size, size, Color.RED);
        updatePosition();
    }

    private void updatePosition() {
        double xOffset = grid.getSceneWidth() / 2;
        double yOffset = grid.getSceneHeight() / 2 - (grid.getRows() + grid.getCols()) * grid.getTileHeight() / 4;

        double x = (col - row) * grid.getTileWidth() / 2 + xOffset - size / 2;
        double y = (col + row) * grid.getTileHeight() / 2 + yOffset - size / 2;

        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    public void moveUp() {
        if (row > 0) {
            row--;
            updatePosition();
        }
    }

    public void moveDown() {
        if (row < grid.getRows() - 1 && (row + col) < grid.getRows() + grid.getCols() - 2) {
            row++;
            updatePosition();
        }
    }

    public void moveLeft() {
        if (col > 0) {
            col--;
            updatePosition();
        }
    }

    public void moveRight() {
        if (col < grid.getCols() - 1 && (row + col) < grid.getRows() + grid.getCols() - 2) {
            col++;
            updatePosition();
        }
    }

    public Rectangle getNode() {
        return node;
    }
}
