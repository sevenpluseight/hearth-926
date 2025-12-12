package com.hearth926.localization;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.*;

public class TextManager {
    private static TextManager instance;
    private Language currentLanguage;

    // Stores all texts keyed by top-level key (UI strings, items, etc.)
    private final Map<String, Object> texts = new HashMap<>();
    private final Map<String, Object> fallbackTexts = new HashMap<>();

    private TextManager(Language language) {
        loadLanguageFiles("en-US", fallbackTexts);   // English fallback
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
        texts.clear();
        loadLanguageFiles(language.getCode(), texts);
    }

    // Load all JSON files in the given language folder into the provided map
    private void loadLanguageFiles(String langCode, Map<String, Object> targetMap) {
        String basePath = "/language/" + langCode;

        try {
            URL url = getClass().getResource(basePath);
            if (url == null) {
                System.out.println("Language folder not found: " + basePath);
                return;
            }

            Path rootPath = Paths.get(url.toURI());

            Files.walk(rootPath)
                    .filter(path -> path.toString().endsWith(".json"))
                    .forEach(jsonPath -> loadSingleJson(jsonPath, targetMap));

        } catch (URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    // Load a single JSON file. Detects NPC dialogue files vs flat text files
    private void loadSingleJson(Path jsonPath, Map<String, Object> targetMap) {
        ObjectMapper mapper = new ObjectMapper();

        try (InputStream is = Files.newInputStream(jsonPath)) {
            Map<String, Object> fileData = mapper.readValue(is, Map.class);

            // Detect NPC dialogue JSON
            if (fileData.containsKey("npc_id") && fileData.containsKey("dialogues")) {
                String npcId = (String) fileData.get("npc_id");
                List<Map<String, Object>> dialogues = (List<Map<String, Object>>) fileData.get("dialogues");

                // Store dialogues under a map with npcId -> dialogueId -> text
                Map<String, Map<String, String>> npcDialogues = new HashMap<>();
                Map<String, String> dialogueMap = new HashMap<>();
                for (Map<String, Object> entry : dialogues) {
                    String dialogueId = (String) entry.get("id");
                    String text = (String) entry.get("text");
                    dialogueMap.put(dialogueId, text);
                }
                npcDialogues.put(npcId, dialogueMap);

                targetMap.putAll(npcDialogues);
            } else {
                // Normal flat JSON
                targetMap.putAll(fileData);
            }

        } catch (IOException e) {
            System.out.println("Failed loading file: " + jsonPath);
            e.printStackTrace();
        }
    }

    // Get flat string by key (UI, items, etc.)
    public String getText(String key) {
        Object value = texts.get(key);
        if (value instanceof String) return (String) value;

        Object fallback = fallbackTexts.get(key);
        if (fallback instanceof String) return (String) fallback;

        return key;
    }

    // Get a list of strings
    public List<String> getTextList(String key) {
        Object value = texts.get(key);
        if (value instanceof List<?>) return (List<String>) value;

        Object fallback = fallbackTexts.get(key);
        if (fallback instanceof List<?>) return (List<String>) fallback;

        return List.of();
    }

    // Get dialogue text for a specific NPC and dialogue ID
    public String getNPCDialogue(String npcId, String dialogueId) {
        Object npcObj = texts.get(npcId);
        if (npcObj instanceof Map<?, ?> npcMap) {
            Object text = npcMap.get(dialogueId);
            if (text instanceof String) return (String) text;
        }

        // fallback
        Object fallbackNpc = fallbackTexts.get(npcId);
        if (fallbackNpc instanceof Map<?, ?> npcMap) {
            Object text = npcMap.get(dialogueId);
            if (text instanceof String) return (String) text;
        }

        return dialogueId; // fallback for debugging
    }

    public Language getCurrentLanguage() {
        return currentLanguage;
    }
}
