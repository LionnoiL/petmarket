package org.petmarket.aws.s3;

public class S3Exception extends RuntimeException {

    public S3Exception(String message) {
        super(message);
    }

    public S3Exception(Throwable cause) {
        super(cause);
    }
}
