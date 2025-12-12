package com.hearth926.localization;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class UIStrings {
    public final StringProperty title = new SimpleStringProperty();
    public final StringProperty startGame = new SimpleStringProperty();
    public final StringProperty continueGame = new SimpleStringProperty();
    public final StringProperty languages = new SimpleStringProperty();

    public static UIStrings load(TextManager textManager) {
        UIStrings ui = new UIStrings();
        ui.title.set(textManager.getText("title"));
        ui.startGame.set(textManager.getText("start_game"));
        ui.continueGame.set(textManager.getText("continue"));
        ui.languages.set(textManager.getText("languages"));
        return ui;
    }

    public void reload(TextManager textManager) {
        title.set(textManager.getText("title"));
        startGame.set(textManager.getText("start_game"));
        continueGame.set(textManager.getText("continue"));
        languages.set(textManager.getText("languages"));
    }
}
