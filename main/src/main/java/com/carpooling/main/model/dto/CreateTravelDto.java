package com.carpooling.main.model.dto;

public class CreateTravelDto {
    private String startingPoint;
    private String destination;
    private String departureTime;
    private int freeSpots;
    private String driverNote;
    private float pricePerPerson;

    public CreateTravelDto() {
    }

    public String getStartingPoint() {
        return startingPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startingPoint = startingPoint;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }

    public String getDriverNote() {
        return driverNote;
    }

    public void setDriverNote(String driverNote) {
        this.driverNote = driverNote;
    }

    public float getPricePerPerson() {
        return pricePerPerson;
    }

    public void setPricePerPerson(float pricePerPerson) {
        this.pricePerPerson = pricePerPerson;
    }
}
