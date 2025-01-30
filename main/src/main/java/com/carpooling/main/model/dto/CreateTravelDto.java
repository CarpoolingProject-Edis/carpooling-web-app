package com.carpooling.main.model.dto;

public class CreateTravelDto {
    private String startPoint;
    private String endPoint;
    private String departure_time;
    private int free_spots;

    public CreateTravelDto() {
    }

    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public String getDeparture_time() {
        return departure_time;
    }

    public void setDeparture_time(String departure_time) {
        this.departure_time = departure_time;
    }

    public int getFree_spots() {
        return free_spots;
    }

    public void setFree_spots(int free_spots) {
        this.free_spots = free_spots;
    }
}
