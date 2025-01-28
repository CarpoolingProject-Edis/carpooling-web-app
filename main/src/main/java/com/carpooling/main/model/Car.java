package com.carpooling.main.model;


import com.carpooling.main.model.enums.VehicleType;
import jakarta.persistence.*;

@Entity
@Table(name = "cars")
public class Car {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "model")
    private String model;
    @Column(name = "year")
    private int year;

    @Enumerated(EnumType.STRING)
    @Column(name = "vehicle_type")
    private VehicleType type;

    public Car() {
    }

    public Car(int id, String model, int year, VehicleType type) {
        this.id = id;
        this.model = model;
        this.year = year;
        this.type = type;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
}
