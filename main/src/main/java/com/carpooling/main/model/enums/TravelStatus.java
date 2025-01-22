package com.carpooling.main.model.enums;

public enum TravelStatus {

    OPEN, CANCELLED, FULL, ONGOING, FINISHED;

    @Override
    public String toString() {
        return switch (this) {
            case CANCELLED -> "Cancelled";
            case FULL -> "Full";
            case ONGOING -> "Ongoing";
            case FINISHED -> "Finished";
            default -> "Open";
        };
    }
}
