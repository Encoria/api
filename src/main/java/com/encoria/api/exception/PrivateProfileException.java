package com.encoria.api.exception;

public class PrivateProfileException extends RuntimeException {
    public PrivateProfileException(String message) {
        super(message);
    }

    public PrivateProfileException() {
        super("User profile is private, access denied.");
    }
}
