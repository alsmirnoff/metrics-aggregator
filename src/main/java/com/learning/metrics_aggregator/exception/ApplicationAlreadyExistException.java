package com.learning.metrics_aggregator.exception;

public class ApplicationAlreadyExistException extends RuntimeException {
    public ApplicationAlreadyExistException(String message) {
        super(message);
    }
}
