package com.encoria.api.exception;

public class ResourceOwnershipException extends RuntimeException {
    public ResourceOwnershipException(String message) {
        super(message);
    }
    public ResourceOwnershipException() {
        super("Resource does not belong to the current user.");
    }
}
