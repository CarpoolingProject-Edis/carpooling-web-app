package com.carpooling.main.exceptions;

public class TravelNotCompletedException extends RuntimeException {
    public TravelNotCompletedException(String message) {
        super(message);
    }
}
