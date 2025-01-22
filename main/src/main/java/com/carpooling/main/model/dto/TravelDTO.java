package com.carpooling.main.model.dto;

import java.time.LocalDateTime;

public class TravelDTO {

    private Integer travelId;
    private Long organizerId;
    private String organizerName;
    private String startPoint;
    private String endPoint;
    private LocalDateTime departureTime;
    private Integer freeSpots;
    private String comments;

    public TravelDTO(Integer travelId, Long organizerId, String organizerName, String startPoint,
                     String endPoint, LocalDateTime departureTime, Integer freeSpots, String comments) {
        this.travelId = travelId;
        this.organizerId = organizerId;
        this.organizerName = organizerName;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.departureTime = departureTime;
        this.freeSpots = freeSpots;
        this.comments = comments;
    }

    public Integer getTravelId() {
        return travelId;
    }

    public void setTravelId(Integer travelId) {
        this.travelId = travelId;
    }

    public Long getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(Long organizerId) {
        this.organizerId = organizerId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
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

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public Integer getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(Integer freeSpots) {
        this.freeSpots = freeSpots;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
