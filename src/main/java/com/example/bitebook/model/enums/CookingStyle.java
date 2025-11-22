package com.example.bitebook.model.enums;

public enum CookingStyle {
    CLASSIC, MODERN, FUSION, MOLECULAR, ETNIC;

    public static CookingStyle fromString(String styleString) throws IllegalArgumentException {
        if (styleString == null || styleString.trim().isEmpty()) {
            throw new IllegalArgumentException("La stringa dello stile di cucina non può essere nulla.");
        }
        return CookingStyle.valueOf(styleString.trim().toUpperCase());
    }
}

