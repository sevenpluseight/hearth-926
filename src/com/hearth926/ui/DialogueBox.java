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
        Rectangle bg = new Rectangle(width, height);
        bg.setFill(Color.WHITE);
        bg.setArcWidth(10);
        bg.setArcHeight(10);

        dialogueLabel = new Label();
        dialogueLabel.setTextFill(Color.BLACK);
        dialogueLabel.setWrapText(true);
        dialogueLabel.setMaxWidth(width - 20);

        VBox container = new VBox(dialogueLabel);
        container.setAlignment(Pos.CENTER);

        getChildren().addAll(bg, container);
        setVisible(false);
    }

    public void showDialogue(String text, int speedMillis) {
        setVisible(true);

        if (typewriterTimeline != null) typewriterTimeline.stop();

        dialogueLabel.setText("");
        final String full = text;

        typewriterTimeline = new Timeline();
        for (int i = 0; i < full.length(); i++) {
            final int idx = i;
            typewriterTimeline.getKeyFrames().add(
                    new KeyFrame(Duration.millis(speedMillis * i),
                            e -> dialogueLabel.setText(full.substring(0, idx + 1)))
            );
        }
        typewriterTimeline.play();
    }

    public void hideDialogue() {
        if (typewriterTimeline != null) typewriterTimeline.stop();
        setVisible(false);
    }

    // Used to block movement
    public boolean isShowing() {
        return isVisible();
    }
}

