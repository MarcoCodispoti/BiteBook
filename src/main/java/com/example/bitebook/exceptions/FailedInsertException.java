package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedInsertException extends Exception {

    @Serial
    private static final long serialVersionUID = 2L;

    public FailedInsertException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedInsertException(Throwable cause) {
        super(cause);
    }
}
