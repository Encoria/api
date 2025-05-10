package com.encoria.api.exception;

public class MomentNotFoundException extends RuntimeException {
    public MomentNotFoundException(String message) {
        super(message);
    }
}
