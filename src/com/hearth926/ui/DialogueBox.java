package com.hearth926.ui;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class DialogueBox extends StackPane {
    private final Label dialogueLabel;
    private Timeline typewriterTimeline;

    public DialogueBox(double width, double height) {
        // Background
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.WHITE);
        bg.setArcWidth(10);
        bg.setArcHeight(10);

        // Dialogue text
        dialogueLabel = new Label();
        dialogueLabel.setTextFill(Color.BLACK);
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMaxWidth(width - 20);

        VBox container = new VBox(dialogueLabel);
        container.setAlignment(Pos.CENTER);
        container.setSpacing(10);

        this.getChildren().addAll(bg, container);
        this.setVisible(false); // initially hidden
    }

    /**
     * Shows the dialogue using typewriter effect
     * @param text the dialogue text
     * @param speedMillis milliseconds per character
     */
    public void showDialogue(String text, int speedMillis) {
        this.setVisible(true);

        // Stop previous animation if any
        if (typewriterTimeline != null) {
            typewriterTimeline.stop();
        }

        dialogueLabel.setText("");
        final String fullText = text;

        typewriterTimeline = new Timeline();
        for (int i = 0; i < fullText.length(); i++) {
            final int index = i;
            KeyFrame keyFrame = new KeyFrame(Duration.millis(speedMillis * i), e -> {
                dialogueLabel.setText(fullText.substring(0, index + 1));
            });
            typewriterTimeline.getKeyFrames().add(keyFrame);
        }
        typewriterTimeline.play();
    }

    public void hideDialogue() {
        if (typewriterTimeline != null) typewriterTimeline.stop();
        this.setVisible(false);
    }
}
