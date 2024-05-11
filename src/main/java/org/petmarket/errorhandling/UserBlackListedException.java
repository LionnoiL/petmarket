package org.petmarket.errorhandling;

public class UserBlackListedException extends RuntimeException {
    public UserBlackListedException(String message) {
        super(message);
    }
}
