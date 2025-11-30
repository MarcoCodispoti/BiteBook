package com.example.bitebook.exceptions;

import java.io.Serial;

public class WrongCredentialsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3L;

    public WrongCredentialsException(String message) {
        super(message);
    }
}
