package com.example.bitebook.exceptions;

import java.io.Serial;

public class QueryException extends Exception{


    @Serial
    private static final long serialVersionUID = 7L;


    public QueryException(Throwable cause) {
        super("Error while executing SQL Query", cause);
    }

}
