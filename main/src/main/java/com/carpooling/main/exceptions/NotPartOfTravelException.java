package com.carpooling.main.exceptions;

public class NotPartOfTravelException extends RuntimeException {

    public NotPartOfTravelException(String message) {
        super(message);
    }
}
