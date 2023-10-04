package org.petmarket.errorhandling;

public class ItemNotCreatedException extends RuntimeException {

    public ItemNotCreatedException(String message) {
        super(message);
    }
}
