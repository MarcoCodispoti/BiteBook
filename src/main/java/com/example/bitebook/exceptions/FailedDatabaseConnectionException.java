package com.example.bitebook.exceptions;

public class FailedDatabaseConnectionException extends RuntimeException {
    public FailedDatabaseConnectionException(String message) {
        super(message );
    }
}
