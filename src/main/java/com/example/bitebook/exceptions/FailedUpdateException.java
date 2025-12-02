package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedUpdateException extends Exception{
    @Serial
    private static final long serialVersionUID = 5L;

    public FailedUpdateException(String message, Throwable cause){
        super(message, cause);
    }

    public FailedUpdateException(Throwable cause){
        super(cause);
    }

    public FailedUpdateException(String message){
        super(message);
    }
}
