package com.hearth926.localization;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

public class TextManager {
    private static TextManager instance;
    private Language currentLanguage;
    private Map<String, String> texts;

    private TextManager(Language language) {
        setLanguage(language);
    }

    // Singleton accessor
    public static TextManager getInstance() {
        if (instance == null) {
            instance = new TextManager(Language.ENGLISH);
        }
        return instance;
    }

    public static void initialize(Language language) {
        instance = new TextManager(language);
    }

    public void setLanguage(Language language) {
        this.currentLanguage = language;
        loadTexts(language);
    }

    private void loadTexts(Language language) {
        ObjectMapper mapper = new ObjectMapper();
        String path = "/localization/" + language.getCode() + ".json";

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) {
                throw new RuntimeException("Localization file not found: " + path);
            }
            texts = mapper.readValue(is, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            texts = Map.of(); // fallback empty map
        }
    }

    public String getText(String key) {
        return texts.getOrDefault(key, key);
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }
}
