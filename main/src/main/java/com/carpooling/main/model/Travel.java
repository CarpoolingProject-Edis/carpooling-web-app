package com.carpooling.main.model;


import com.carpooling.main.model.enums.TravelStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "travels")
public class Travel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "startPoint")
    private String startPoint;

    @Column(name = "endPoint")
    private String endPoint;

    @Column(name = "departure_time")
    private LocalDateTime departureTime;

    @Column(name = "free_spots")
    private int freeSpots;

    @ManyToOne
    @JoinColumn(name = "driver_id")
    private User driver;

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_applications",
            joinColumns = @JoinColumn(name = "travel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<TravelRequest> requests = new HashSet<>();

    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "travels_passengers",
            joinColumns = @JoinColumn(name = "travel_id"),
            inverseJoinColumns = @JoinColumn(name = "passenger_id")
    )
    private Set<User> passengers = new HashSet<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private TravelStatus travelStatus;

    @Column(name = "duration")
    private String duration;
    @Column(name = "arrival_time")
    private LocalDateTime arrivalTime;


    public Travel() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getStartingPoint() {
        return startPoint;
    }

    public void setStartingPoint(String startingPoint) {
        this.startPoint = startingPoint;
    }

    public String getDestination() {
        return endPoint;
    }

    public void setDestination(String destination) {
        this.endPoint = destination;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public LocalDateTime getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(LocalDateTime departureTime) {
        this.departureTime = departureTime;
    }

    public int getFreeSpots() {
        return freeSpots;
    }

    public void setFreeSpots(int freeSpots) {
        this.freeSpots = freeSpots;
    }


    public User getDriver() {
        return driver;
    }

    public void setDriver(User driver) {
        this.driver = driver;
    }

    public TravelStatus getTravelStatus() {
        return travelStatus;
    }

    public void setTravelStatus(TravelStatus travelStatus) {
        this.travelStatus = travelStatus;
    }

    public Set<TravelRequest> getRequests() {
        return requests;
    }

    public void setRequests(Set<TravelRequest> requests) {
        this.requests = requests;
    }

    public Set<User> getPassengers() {
        return passengers;
    }

    public void setPassengers(Set<User> passengers) {
        this.passengers = passengers;
    }

    public void addPassenger(User user) {
        passengers.add(user);
    }

    public void removePassenger(User user) {
        passengers.remove(user);
    }

    public LocalDateTime getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(LocalDateTime arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Travel travel = (Travel) o;
        return id == travel.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
