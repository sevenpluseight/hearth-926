package com.hearth926.game;

import com.hearth926.localization.Language;
import com.hearth926.localization.TextManager;
import com.hearth926.localization.UIStrings;
import javafx.animation.FadeTransition;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class TitleScene {
    private final Scene scene;
    private final TextManager textManager;
    private final UIStrings uiStrings;

    public TitleScene(double width, double height, Runnable onStart) {
        textManager = TextManager.getInstance();
        uiStrings = UIStrings.load(textManager);

        VBox root = new VBox(40);
        root.setPrefSize(width, height);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: linear-gradient(to bottom, #87CEEB, #1E90FF);");

        // Title
        Text titleText = new Text();
        titleText.setFont(Font.font(64));
        titleText.setStyle("-fx-fill: gold; -fx-stroke: black; -fx-stroke-width: 2;");
        titleText.textProperty().bind(uiStrings.title); // bind to property

        // Start, Continue, Language (text nodes)
        Text startText = new Text();
        startText.setFont(Font.font(24));
        startText.textProperty().bind(uiStrings.startGame);

        Text continueText = new Text();
        continueText.setFont(Font.font(24));
        continueText.textProperty().bind(uiStrings.continueGame);

        Text languageText = new Text();
        languageText.setFont(Font.font(24));
        languageText.textProperty().bind(uiStrings.languages);

        HBox buttonBox = new HBox(20, startText, continueText, languageText);
        buttonBox.setAlignment(Pos.CENTER);

        root.getChildren().addAll(titleText, buttonBox);

        scene = new Scene(root, width, height);

        // Language toggle logic
        languageText.setOnMouseClicked(e -> toggleLanguage(onStart));

        // Start interaction
        startText.setOnMouseClicked(e -> fadeToGame(onStart, root));
    }

    private void toggleLanguage(Runnable onStart) {
        if (textManager.getCurrentLanguage() == Language.ENGLISH) {
            textManager.setLanguage(Language.MANDARIN);
        } else {
            textManager.setLanguage(Language.ENGLISH);
        }

        // Reload UIStrings properties
        uiStrings.reload(textManager);
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
