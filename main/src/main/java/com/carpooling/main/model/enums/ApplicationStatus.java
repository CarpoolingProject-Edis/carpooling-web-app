package com.carpooling.main.model.enums;

public enum ApplicationStatus {
    PENDING, DECLINED, APPROVED;

    @Override
    public String toString() {
        return switch (this) {
            case DECLINED -> "Declined";
            case APPROVED -> "Approved";
            default -> "Pending";
        };
    }
}
