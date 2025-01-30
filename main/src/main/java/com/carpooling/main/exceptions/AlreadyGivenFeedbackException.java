package com.carpooling.main.exceptions;

public class AlreadyGivenFeedbackException extends RuntimeException {
    public AlreadyGivenFeedbackException(String message) {
        super(message);
    }
}
