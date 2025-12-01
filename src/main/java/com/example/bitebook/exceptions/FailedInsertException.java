package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedInsertException extends Exception {

    @Serial
    private static final long serialVersionUID = 2L;

    public FailedInsertException(String message) {
        super(message);
    }
}
