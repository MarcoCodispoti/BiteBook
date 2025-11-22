package com.example.bitebook.exceptions;

public class WrongCredentialsExcpetion extends RuntimeException {
    public WrongCredentialsExcpetion(String message) {
        super(message);
    }
}
