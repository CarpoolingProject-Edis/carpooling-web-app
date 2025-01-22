package com.carpooling.main.model.enums;

public enum UserStatus {
    ACTIVE, BLOCKED;

    @Override
    public String toString() {
        return switch (this) {
            case BLOCKED -> "Blocked";
            default -> "Active";
        };
    }
}
