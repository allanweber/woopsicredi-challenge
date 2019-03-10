package com.allanweber.woopsicredi.domain.exception;

public class DataNotFoundedException extends RuntimeException {
    public DataNotFoundedException(String message) {
        super(message);
    }
}
