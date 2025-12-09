package com.hearth926.player;

import com.hearth926.grid.Grid;
import com.hearth926.player.enums.Direction;
import javafx.animation.Interpolator;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Player {
    private int row;
    private int col;
    private boolean moving = false;
    private final Rectangle node;
    private final Grid grid;
    private static final double SIZE = 15;
    private static final Duration MOVE_TIME = Duration.millis(135);

    public Player(int row, int col, Grid grid) {
        this.row = row;
        this.col = col;
        this.grid = grid;
//        this.node = new Rectangle (size, size, Color.RED);

        node = new Rectangle(SIZE, SIZE, Color.RED);
//        updatePosition();
        setInstantPosition();
    }

//    private void updatePosition() {
//        double xOffset = grid.getSceneWidth() / 2;
//        double yOffset = grid.getSceneHeight() / 2 - (grid.getRows() + grid.getCols()) * grid.getTileHeight() / 4;
//
//        double x = (col - row) * grid.getTileWidth() / 2 + xOffset - size / 2;
//        double y = (col + row) * grid.getTileHeight() / 2 + yOffset - size / 2;
//
//        node.setTranslateX(x);
//        node.setTranslateY(y);
//    }
//
//    public void moveUp() {
//        if (row > 0) {
//            row--;
//            updatePosition();
//        }
//    }
//
//    public void moveDown() {
//        if (row < grid.getRows() - 1 && (row + col) < grid.getRows() + grid.getCols() - 2) {
//            row++;
//            updatePosition();
//        }
//    }
//
//    public void moveLeft() {
//        if (col > 0) {
//            col--;
//            updatePosition();
//        }
//    }
//
//    public void moveRight() {
//        if (col < grid.getCols() - 1 && (row + col) < grid.getRows() + grid.getCols() - 2) {
//            col++;
//            updatePosition();
//        }
//    }
//
//    public Rectangle getNode() {
//        return node;
//    }

    // Initial placement
    private void setInstantPosition() {
        node.setTranslateX(calculateX(col, row));
        node.setTranslateY(calculateY(col, row));
    }

    private double calculateX(int col, int row) {
        double xOffset = grid.getSceneWidth() / 2;
        return (col - row) * grid.getTileWidth() / 2 + xOffset - SIZE / 2;
    }

    private double calculateY(int col, int row) {
        double yOffset = grid.getSceneHeight() / 2 - (grid.getRows() + grid.getCols()) * grid.getTileHeight() / 4;
        return (col + row) * grid.getTileHeight() / 2 + yOffset - SIZE / 2;
    }

    // Public controls
    public void move(Direction direction) {
        if (moving) return;

        int targetRow = row;
        int targetCol = col;

        switch (direction) {
            case UP -> targetRow--;
            case DOWN -> targetRow++;
            case LEFT -> targetCol--;
            case RIGHT -> targetCol++;

            case UP_LEFT -> {
                targetRow--;
                targetCol--;
            }
            case UP_RIGHT -> {
                targetRow--;
                targetCol++;
            }
            case DOWN_LEFT -> {
                targetRow++;
                targetCol--;
            }
            case DOWN_RIGHT -> {
                targetRow++;
                targetCol++;
            }
        }

        if (!isValid(targetRow, targetCol)) return;

        animateMove(targetRow, targetCol);
    }

    // Core logic
    private void animateMove(int newRow, int newCol) {
        moving = true;

        double newX = calculateX(newCol, newRow);
        double newY = calculateY(newCol, newRow);

        TranslateTransition move = new TranslateTransition(MOVE_TIME, node);
        move.setToX(newX);
        move.setToY(newY);
        move.setInterpolator(Interpolator.EASE_BOTH);

        move.setOnFinished(e -> {
            row = newRow;
            col = newCol;
            moving = false;
        });

        move.play();
    }

    private boolean isValid(int row, int col) {
        return row >= 0 && col >= 0 && row < grid.getRows() && col < grid.getCols();
    }

    public Rectangle getNode() {
        return node;
    }
}
