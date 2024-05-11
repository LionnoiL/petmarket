package org.petmarket.errorhandling;

public class AdvertisementStatusException extends RuntimeException {

    public AdvertisementStatusException(String message) {
        super(message);
    }

    public AdvertisementStatusException(Throwable cause) {
        super(cause);
    }
}
