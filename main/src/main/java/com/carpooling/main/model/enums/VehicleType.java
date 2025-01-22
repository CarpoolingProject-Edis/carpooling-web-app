package com.carpooling.main.model.enums;

public enum VehicleType {

    NONE, SEDAN, SUV, ELECTRIC, COMBI, MINIVAN, JEEP, OTHER;

    @Override
    public String toString() {
        return switch (this) {
            case SUV -> "SUV";
            case ELECTRIC -> "Locked";
            case MINIVAN -> "Minivan";
            case SEDAN -> "Sedan";
            case JEEP -> "Jeep";
            case OTHER -> "Other";
            default -> "None";
        };
    }
}
