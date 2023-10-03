package org.petmarket.errorhandling;

import org.springframework.security.core.AuthenticationException;

public class UserNotFoundException extends AuthenticationException {

    public UserNotFoundException(String msg) {
        super(msg);
    }
}
