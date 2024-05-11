package org.petmarket.errorhandling;

public class UserBlackListedException extends RuntimeException {
    public UserBlackListedException(String message) {
        super(message);
    }

    public UserBlackListedException(Throwable cause) {
        super(cause);
    }
}
