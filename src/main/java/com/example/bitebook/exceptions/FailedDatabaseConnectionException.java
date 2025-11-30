package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedDatabaseConnectionException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    public FailedDatabaseConnectionException(String message) {
        super(message );
    }
}
