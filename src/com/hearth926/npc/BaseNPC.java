package com.hearth926.npc;

import com.hearth926.player.Player;
import com.hearth926.worldLayer.Grid;
import com.hearth926.interaction.Interactable;
import com.hearth926.interaction.InteractionRange;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.function.Consumer;

public abstract class BaseNPC implements Interactable {
    protected final Rectangle node;
    protected final int rowStart;
    protected final int colStart;
    protected int row;
    protected int col;
    protected static final double SIZE = 18;
    protected static final double INTERACTION_RANGE = 50;

    protected final double interactionRange;
    protected final NPCType npcType;
    protected boolean moving = false;
    protected static final Duration MOVE_DURATION = Duration.millis(300);

    protected Consumer<Player> onInteractCallback = null;

    public BaseNPC(int row, int col, Grid grid, Color color, NPCType npcType, double interactionRange) {
        this.rowStart = this.row = row;
        this.colStart = this.col = col;
        this.npcType = npcType;
        this.interactionRange = interactionRange;

        this.node = new Rectangle(SIZE, SIZE, color);
        snapToTile(grid);
    }

    public void snapToTile(Grid grid) {
        double xOffset = grid.getSceneWidth() / 2;
        double yOffset = grid.getSceneHeight() / 2 - (grid.getRows() + grid.getCols()) * grid.getTileHeight() / 4;

        double x = (col - row) * grid.getTileWidth() / 2 + xOffset - SIZE / 2;
        double y = (col + row) * grid.getTileHeight() / 2 + yOffset - SIZE / 2;

        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    @Override
    public void interact(Player player) {
        if (onInteractCallback != null) {
            onInteractCallback.accept(player);
        } else {
            defaultInteract(player);
        }
    }

    protected void defaultInteract(Player player) {
        System.out.println("Interacted with " + npcType + " at (" + row + ", " + col + ")");
    }

    public void setOnInteract(Consumer<Player> callback) {
        this.onInteractCallback = callback;
    }

    public void moveTo(int targetRow, int targetCol, Grid grid) {
        if (moving) return;
        moving = true;

        double xOffset = grid.getSceneWidth() / 2;
        double yOffset = grid.getSceneHeight() / 2 - (grid.getRows() + grid.getCols()) * grid.getTileHeight() / 4;

        double newX = (targetCol - targetRow) * grid.getTileWidth() / 2 + xOffset - SIZE / 2;
        double newY = (targetCol + targetRow) * grid.getTileHeight() / 2 + yOffset - SIZE / 2;

        TranslateTransition move = new TranslateTransition(MOVE_DURATION, node);
        move.setToX(newX);
        move.setToY(newY);
        move.setOnFinished(e -> {
            this.row = targetRow;
            this.col = targetCol;
            moving = false;
        });
        move.play();
    }

    @Override
    public boolean isInRange(Player player) {
        return InteractionRange.inRange(player, node, INTERACTION_RANGE);
    }

    // Getters
    public Rectangle getNode() { return node; }
    public int getRow() { return row; }
    public int getCol() { return col; }
    public NPCType getNpcType() { return npcType; }
}
