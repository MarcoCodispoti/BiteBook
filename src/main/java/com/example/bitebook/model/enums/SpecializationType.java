package com.example.bitebook.model.enums;

public enum SpecializationType {
    ITALIAN, FRENCH, JAPANESE, VEGETARIAN, VEGAN, PASTRY, SEAFOOD;

    public static SpecializationType fromString(String specializationString) throws IllegalArgumentException {
        if (specializationString == null || specializationString.trim().isEmpty()) {
            // Gestione di input nulli o vuoti, sebbene il campo nel DB sia NOT NULL.
            throw new IllegalArgumentException("La stringa della specializzazione non può essere nulla o vuota.");
        }

        // La funzione valueOf di Enum è il modo più sicuro per la conversione.
        // Convertiamo in maiuscolo per uniformità, dato che gli Enum Java sono per convenzione in maiuscolo.
        return SpecializationType.valueOf(specializationString.trim().toUpperCase());
    }
}
