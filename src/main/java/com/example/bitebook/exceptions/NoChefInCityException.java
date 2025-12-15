package com.example.bitebook.exceptions;

import java.io.Serial;

public class NoChefInCityException extends Exception {


    @Serial
    private static final long serialVersionUID = 6L;


    public NoChefInCityException(String message) {
        super(message);
    }

}
