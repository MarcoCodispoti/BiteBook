package com.example.bitebook.exceptions;

import java.io.Serial;

public class NoChefInCityException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2L;

    public NoChefInCityException(String message) {
        super(message);
    }
}
