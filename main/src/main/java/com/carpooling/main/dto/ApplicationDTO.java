package com.carpooling.main.dto;

import com.carpooling.main.enums.ApplicationStatus;

import java.time.LocalDateTime;

public class ApplicationDTO {

    private Integer applicationId;
    private Integer travelId;
    private String travelStartPoint;
    private String travelEndPoint;
    private Long userId;
    private String userName;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    public ApplicationDTO(Integer applicationId, Integer travelId, String travelStartPoint,
                          String travelEndPoint, Long userId, String userName,
                          ApplicationStatus status, LocalDateTime appliedAt) {
        this.applicationId = applicationId;
        this.travelId = travelId;
        this.travelStartPoint = travelStartPoint;
        this.travelEndPoint = travelEndPoint;
        this.userId = userId;
        this.userName = userName;
        this.status = status;
        this.appliedAt = appliedAt;
    }

    public Integer getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Integer applicationId) {
        this.applicationId = applicationId;
    }

    public Integer getTravelId() {
        return travelId;
    }

    public void setTravelId(Integer travelId) {
        this.travelId = travelId;
    }

    public String getTravelStartPoint() {
        return travelStartPoint;
    }

    public void setTravelStartPoint(String travelStartPoint) {
        this.travelStartPoint = travelStartPoint;
    }

    public String getTravelEndPoint() {
        return travelEndPoint;
    }

    public void setTravelEndPoint(String travelEndPoint) {
        this.travelEndPoint = travelEndPoint;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public ApplicationStatus getStatus() {
        return status;
    }

    public void setStatus(ApplicationStatus status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }
}
