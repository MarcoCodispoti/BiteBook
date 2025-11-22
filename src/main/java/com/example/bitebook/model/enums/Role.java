package com.example.bitebook.model.enums;

public enum Role {
    CLIENT, CHEF;

    public static Role fromString(String roleString) throws IllegalArgumentException {

        // 1. Validazione base: Controlla se l'input è nullo o vuoto
        if (roleString == null || roleString.trim().isEmpty()) {
            // Se la query DB restituisce NULL, la logica di login deve gestirlo come 'utente non trovato'.
            // Qui rilanciamo un'eccezione, se un ruolo è obbligatorio nel contesto.
            throw new IllegalArgumentException("La stringa del ruolo non può essere nulla o vuota.");
        }

        // 2. Normalizzazione: Converte in maiuscolo per matchare i nomi dell'Enum Java
        String normalizedRole = roleString.trim().toUpperCase();

        try {
            // 3. Conversione: Utilizza il metodo standard valueOf()
            return Role.valueOf(normalizedRole);

        } catch (IllegalArgumentException e) {
            // 4. Fallback/Errore: Se la stringa non corrisponde a nessun nome Enum
            // (es. il DB contenesse "ADMIN" che non è definito qui).
            throw new IllegalArgumentException("Ruolo non valido nel database: '" + roleString +
                    "'. Ruoli supportati: CLIENT, CHEF.", e);
        }
    }
}
