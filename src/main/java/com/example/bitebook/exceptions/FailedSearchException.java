package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedSearchException extends Exception{
    @Serial
    private static final long serialVersionUID = 4L;

    public FailedSearchException(String message, Throwable cause){
        super(message, cause);
    }

    public FailedSearchException(Throwable cause){
        super(cause);
    }
}
