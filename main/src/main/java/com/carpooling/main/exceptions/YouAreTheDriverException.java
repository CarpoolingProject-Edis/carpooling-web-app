package com.carpooling.main.exceptions;

public class YouAreTheDriverException extends RuntimeException {
    public YouAreTheDriverException(String message) {
        super(message);
    }
}
