package com.hearth926.game;

import com.hearth926.localization.Language;
import com.hearth926.localization.TextManager;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TitleScene {
    private final Scene scene;
    private final TextManager textManager;

    public TitleScene(double width, double height, Runnable onStart) {
        textManager = TextManager.getInstance(); // default is English

        VBox root = new VBox(40);
        root.setPrefSize(width, height);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-alignment: center; -fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");

        // Game title
        Text title = new Text("Hearth:926");
        title.setFont(Font.font(64));
        title.setStyle("-fx-fill: gold; -fx-stroke: black; -fx-stroke-width: 2;");

        // Buttons container
        HBox buttonBox = new HBox(20);
        buttonBox.setAlignment(Pos.CENTER);

        // Start Game button
        Button startButton = new Button(textManager.getText("start"));
        startButton.setFont(Font.font(24));
        startButton.setCursor(Cursor.HAND);
        startButton.setOnAction(e -> fadeToGame(onStart, root));

        // Continue Game button
        Button continueButton = new Button(textManager.getText("continue"));
        continueButton.setFont(Font.font(24));
        continueButton.setDisable(true);
        continueButton.setStyle("-fx-opacity: 0.6;");
        continueButton.setCursor(Cursor.DEFAULT);

        // Languages button
        Button languageButton = new Button(textManager.getText("languages"));
        languageButton.setFont(Font.font(24));
        languageButton.setCursor(Cursor.HAND);
        languageButton.setOnAction(e -> {
            // Toggle language
            if (textManager.getCurrentLanguage() == Language.ENGLISH) {
                textManager.setLanguage(Language.MANDARIN);
            } else {
                textManager.setLanguage(Language.ENGLISH);
            }

            // Update UI
            startButton.setText(textManager.getText("start"));
            continueButton.setText(textManager.getText("continue"));
            languageButton.setText(textManager.getText("languages"));
            title.setText(textManager.getText("title"));
        });

        buttonBox.getChildren().addAll(startButton, continueButton, languageButton);
        root.getChildren().addAll(title, buttonBox);

        scene = new Scene(root, width, height);
    }

    private void fadeToGame(Runnable onStart, VBox root) {
        FadeTransition fade = new FadeTransition(Duration.seconds(1), root);
        fade.setFromValue(1.0);
        fade.setToValue(0.0);
        fade.setOnFinished(e -> onStart.run());
        fade.play();
    }

    public Scene getScene() {
        return scene;
    }
}
