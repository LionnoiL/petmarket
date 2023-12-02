package org.petmarket.errorhandling;

public class FileUploadException extends RuntimeException {

    public FileUploadException(String message) {
        super(message);
    }
}
