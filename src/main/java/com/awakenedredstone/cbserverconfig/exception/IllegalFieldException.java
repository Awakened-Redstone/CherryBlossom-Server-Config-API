package com.awakenedredstone.cbserverconfig.exception;

public class IllegalFieldException extends RuntimeException {

    public IllegalFieldException(String s) {
        super(s);
    }

    public IllegalFieldException(String message, Throwable cause) {
        super(message, cause);
    }
}
