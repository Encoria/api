package com.encoria.api.exception;

public class FollowSelfException extends RuntimeException {
    public FollowSelfException(String message) {
        super(message);
    }

    public FollowSelfException() {
        super("Cannot follow self");
    }
}
