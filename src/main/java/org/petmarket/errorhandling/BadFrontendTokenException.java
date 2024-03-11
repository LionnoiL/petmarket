package org.petmarket.errorhandling;

public class BadFrontendTokenException extends RuntimeException {

    public BadFrontendTokenException(String message) {
        super(message);
    }
}
