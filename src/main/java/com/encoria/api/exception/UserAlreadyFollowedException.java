package com.encoria.api.exception;

public class UserAlreadyFollowedException extends RuntimeException {
    public UserAlreadyFollowedException(String message) {
        super(message);
    }

    public UserAlreadyFollowedException() {
        super("User is already followed.");
    }
}
