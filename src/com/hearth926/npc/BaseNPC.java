package com.hearth926.npc;

import com.hearth926.worldLayer.Grid;
import com.hearth926.interaction.Interactable;
import com.hearth926.interaction.InteractionRange;
import com.hearth926.localization.TextManager;
import com.hearth926.player.Player;
import javafx.animation.TranslateTransition;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.List;
import java.util.function.Consumer;

public abstract class BaseNPC implements Interactable {
    // Visual and positioning
    protected final Rectangle node;
    protected final int rowStart;
    protected final int colStart;
    protected int row;
    protected int col;
    protected static final double SIZE = 18;
    protected static final double INTERACTION_RANGE = 50;

    // Interaction
    protected final double interactionRange;

    // NPC type
    protected final NPCType npcType;

    // Dialogue and quest
    protected String dialogueKey;
    protected List<String> dialogueLines;
    protected int dialogueIndex = 0;
    protected boolean hasQuest = false;

    // Movement
    protected boolean moving = false;
    protected static final Duration MOVE_DURATION = Duration.millis(300);

    // Custom interaction
    protected Consumer<Player> onInteractCallback = null;

    /**
     * Constructor
     */
    public BaseNPC(int row, int col, Grid grid, Color color, NPCType npcType, String dialogueKey, double interactionRange) {
        this.rowStart = this.row = row;
        this.colStart = this.col = col;
        this.npcType = npcType;
        this.dialogueKey = dialogueKey;
        this.interactionRange = interactionRange;

        this.node = new Rectangle(SIZE, SIZE, color);
        snapToTile(grid);
        preloadDialogue();
    }

    // Snap NPC to tile
    public void snapToTile(Grid grid) {
        double xOffset = grid.getSceneWidth() / 2;
        double yOffset = grid.getSceneHeight() / 2 - (grid.getRows() + grid.getCols()) * grid.getTileHeight() / 4;

        double x = (col - row) * grid.getTileWidth() / 2 + xOffset - SIZE / 2;
        double y = (col + row) * grid.getTileHeight() / 2 + yOffset - SIZE / 2;

        node.setTranslateX(x);
        node.setTranslateY(y);
    }

    // Default interaction; can be overridden or use custom callback
    @Override
    public void interact(Player player) {
        if (onInteractCallback != null) {
            onInteractCallback.accept(player);
        } else if (dialogueLines != null && !dialogueLines.isEmpty()) {
            startDialogue();
        } else {
            System.out.println("Interacted with " + npcType + " at (" + row + ", " + col + ")");
        }
    }

    // Register a custom interaction callback
    public void setOnInteract(Consumer<Player> callback) {
        this.onInteractCallback = callback;
    }

    // Preload dialogue from TextManager
    protected void preloadDialogue() {
        if (dialogueKey != null) {
            dialogueLines = TextManager.getInstance().getTextList(dialogueKey);
            if (dialogueLines.isEmpty()) {
                // fallback to English if missing
                dialogueLines = TextManager.getInstance().getTextList(dialogueKey);
            }
        }
    }

    // Start or continue dialogue
    public void startDialogue() {
        if (dialogueLines == null || dialogueLines.isEmpty()) return;
//        dialogueIndex = 0;
        showNextDialogue();
    }

    // Show the next dialogue line
    public void showNextDialogue() {
        if (dialogueLines == null || dialogueLines.isEmpty()) return;

        if (dialogueIndex < dialogueLines.size()) {
            System.out.println(npcType + " says: " + dialogueLines.get(dialogueIndex));
            dialogueIndex++;
        } else {
            dialogueIndex = 0; // Optional: reset after finishing all lines
        }
    }

    // Quest system
    public void enableQuest() { hasQuest = true; }
    public void disableQuest() { hasQuest = false; }
    public boolean hasQuest() { return hasQuest; }

    // Move NPC to target tile
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

    // Check if player is in the interaction range
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
