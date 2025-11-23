package com.example.bitebook.exceptions;

public class EmptyResultSetException extends RuntimeException {
    public EmptyResultSetException(String message) {
        super(message);
    }
}
