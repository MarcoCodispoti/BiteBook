package com.example.bitebook.exceptions;

import java.io.Serial;

public class WrongCredentialsException extends Exception {


    @Serial
    private static final long serialVersionUID = 8L;


    public WrongCredentialsException(String message) {
        super(message);
    }

}
