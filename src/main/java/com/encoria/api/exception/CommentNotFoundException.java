package com.encoria.api.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException(String message) {
        super(message);
    }

    public CommentNotFoundException() {
        super("Comment not found");
    }
}
