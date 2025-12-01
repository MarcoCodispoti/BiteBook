package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedDatabaseConnectionException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    // Costruttore completo
    public FailedDatabaseConnectionException(String message, Throwable cause) {
        super(message, cause); // Passa tutto al padre, molto più pulito di initCause
    }

    // Costruttore rapido (messaggio default)
    public FailedDatabaseConnectionException(Throwable cause) {
        super("Impossibile connettersi al database", cause);
    }

}
