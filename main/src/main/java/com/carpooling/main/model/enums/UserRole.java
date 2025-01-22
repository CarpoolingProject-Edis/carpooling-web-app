package com.carpooling.main.model.enums;

public enum UserRole {
    ADMIN, USER, GUEST;

    @Override
    public String toString() {
        return switch (this) {
            case ADMIN -> "Admin";
            case USER -> "User";
            default -> "Guest";
        };
    }
}
