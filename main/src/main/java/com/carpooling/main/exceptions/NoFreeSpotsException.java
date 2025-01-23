package com.carpooling.main.exceptions;

public class NoFreeSpotsException extends RuntimeException {
    public NoFreeSpotsException(String message) {
        super(message);
    }
}
