package com.ToNyQwW.payment.jdbc.exception;

public class PropertyLoadException extends RuntimeException {

    public PropertyLoadException(Throwable cause) {
        super(cause);
    }

    public PropertyLoadException(String message) {
        super(message);
    }
}
