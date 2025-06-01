package com.encoria.api.exception;

public class PublicationAlreadyLikedException extends RuntimeException {
  public PublicationAlreadyLikedException(String message) {
    super(message);
  }

  public PublicationAlreadyLikedException() {
    super("User already liked this publication");
  }
}
