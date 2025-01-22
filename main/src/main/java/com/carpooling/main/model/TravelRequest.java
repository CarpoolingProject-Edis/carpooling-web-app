package com.carpooling.main.model;


import com.carpooling.main.model.enums.ApplicationStatus;
import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "user_applications")
public class TravelRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User passenger;

    @OneToOne
    @JoinColumn(name = "travel_id")
    private Travel travel;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ApplicationStatus applicationStatus;

    public TravelRequest() {
    }

    public TravelRequest(int id, User passenger, Travel travel, ApplicationStatus applicationStatus) {
        this.id = id;
        this.passenger = passenger;
        this.travel = travel;
        this.applicationStatus = applicationStatus;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getPassenger() {
        return passenger;
    }

    public void setPassenger(User passenger) {
        this.passenger = passenger;
    }

    public Travel getTravel() {
        return travel;
    }

    public void setTravel(Travel travel) {
        this.travel = travel;
    }

    public ApplicationStatus getApplicationStatus() {
        return applicationStatus;
    }

    public void setApplicationStatus(ApplicationStatus applicationStatus) {
        this.applicationStatus = applicationStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TravelRequest that)) return false;
        return id == that.id && Objects.equals(passenger, that.passenger) && Objects.equals(travel, that.travel) && applicationStatus == that.applicationStatus;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, passenger, travel, applicationStatus);
    }
}
