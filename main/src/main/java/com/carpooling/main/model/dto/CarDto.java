package com.carpooling.main.model.dto;


import com.carpooling.main.model.enums.VehicleType;
import jakarta.validation.constraints.NotEmpty;

public class CarDto {
    @NotEmpty
    private String model;
    private int year;
    private VehicleType type;

    public CarDto() {
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}
