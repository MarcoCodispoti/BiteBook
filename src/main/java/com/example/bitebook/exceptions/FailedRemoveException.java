package com.example.bitebook.exceptions;

import java.io.Serial;

public class FailedRemoveException extends Exception {

    @Serial
    private static final long serialVersionUID = 3L;


    public FailedRemoveException(Throwable cause){
        super(cause);
    }

}
