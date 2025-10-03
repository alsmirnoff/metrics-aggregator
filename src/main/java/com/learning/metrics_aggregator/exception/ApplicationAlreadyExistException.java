package com.learning.metrics_aggregator.exception;

public class ApplicationAlreadyExistException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApplicationAlreadyExistException(String message) {
        super(message);
    }
}
