package com.encoria.api.exception;

public class PublicationNotFoundException extends RuntimeException {
    public PublicationNotFoundException(String message) {
        super(message);
    }

    public PublicationNotFoundException() {
        super("Publication not found");
    }
}
