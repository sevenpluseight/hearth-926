package com.hearth926.localization;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TextManager {
    private static TextManager instance;
    private Language currentLanguage;
    private Map texts;
    private Map fallbackTexts; // fallback language (English)

    private TextManager(Language language) {
        loadFallbackTexts();
        setLanguage(language);
    }

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

    // Load the main language
    private void loadTexts(Language language) {
        ObjectMapper mapper = new ObjectMapper();
        String path = "/localization/" + language.getCode() + ".json";

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("Localization file not found: " + path);
            texts = mapper.readValue(is, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            texts = Map.of(); // fallback to empty
        }
    }

    // Load fallback English
    private void loadFallbackTexts() {
        ObjectMapper mapper = new ObjectMapper();
        String path = "/localization/en.json";

        try (InputStream is = getClass().getResourceAsStream(path)) {
            if (is == null) throw new RuntimeException("Fallback localization file not found: " + path);
            fallbackTexts = mapper.readValue(is, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            fallbackTexts = Map.of();
        }
    }

    // Get a single string by key
    public String getText(String key) {
        Object value = texts.get(key);
        if (value instanceof String) return (String) value;

        // fallback to English
        Object fallbackValue = fallbackTexts.get(key);
        if (fallbackValue instanceof String) return (String) fallbackValue;

        return key; // ultimate fallback
    }

    // Get a list of strings
    public List<String> getTextList(String key) {
        Object value = texts.get(key);
        if (value instanceof List<?>) return (List<String>) value;

        // fallback to English
        Object fallbackValue = fallbackTexts.get(key);
        if (fallbackValue instanceof List<?>) return (List<String>) fallbackValue;

        return List.of(); // ultimate fallback
    }

    public Language getCurrentLanguage() { return currentLanguage; }
}
