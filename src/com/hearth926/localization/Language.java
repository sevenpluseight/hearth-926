package com.hearth926.localization;

public enum Language {
    ENGLISH("en"),
    MANDARIN("zh-CN");

    private final String code;

    Language(String code) { this.code = code; }

    public String getCode() { return code; }
}
