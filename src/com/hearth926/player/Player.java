package com.hearth926.player;

import com.hearth926.worldLayer.Grid;
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

        node = new Rectangle(SIZE, SIZE, Color.RED);
        setInstantPosition();
    }

    private void setInstantPosition() {
        node.setTranslateX(calculateX(col, row));
        node.setTranslateY(calculateY(col, row));
    }

    private double calculateX(int col, int row) {
        return (col - row) * grid.getTileWidth() / 2 + grid.getXOffset() - SIZE / 2;
    }

    private double calculateY(int col, int row) {
        return (col + row) * grid.getTileHeight() / 2 + grid.getYOffset() - SIZE / 2;
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
//        return row >= 0 && col >= 0 && row < grid.getRows() && col < grid.getCols();

        // Map bounds check
        if (row < 0 || col < 0 || row >= grid.getRows() || col >= grid.getCols()) return false;

        // Check for NPC collision
        return !grid.isTileOccupied(row, col);
    }

    public Rectangle getNode() {
        return node;
    }

    public double getCenterX() {
        return node.getTranslateX() + SIZE / 2;
    }

    public double getCenterY() {
        return node.getTranslateY() + SIZE / 2;
    }

    public boolean isMoving() {
        return moving;
    }

    // Optional: get tile coordinates
    public int getRow() { return row; }
    public int getCol() { return col; }
}
