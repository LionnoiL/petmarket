package org.petmarket.errorhandling;

public class ItemNotUpdatedException extends RuntimeException{

    public ItemNotUpdatedException(String message) {
        super(message);
    }
}
