package com.encoria.api.exception;

public class MomentNotFoundException extends RuntimeException {
    public MomentNotFoundException(String message) {
        super(message);
    }

    public MomentNotFoundException() {
        super("Moment not found.");
    }
}
