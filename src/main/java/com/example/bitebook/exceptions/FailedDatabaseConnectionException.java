package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedDatabaseConnectionException extends Exception {

    @Serial
    private static final long serialVersionUID = 1L;

    public FailedDatabaseConnectionException(String message){
        super(message);
    }

    public FailedDatabaseConnectionException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedDatabaseConnectionException(Throwable cause) {
        super("Unable to connect to the database", cause);
    }

}
