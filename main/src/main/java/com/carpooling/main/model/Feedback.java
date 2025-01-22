package com.carpooling.main.model;

import jakarta.persistence.*;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @ManyToOne
    @JoinColumn(name = "giver_id")
    private User createdBy;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private User receivedBy;

    @ManyToOne
    @JoinColumn(name = "travel_id")
    private Travel travelToRate;

    @Column(name = "rating")
    private int rating;

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(User createdBy) {
        this.createdBy = createdBy;
    }

    public User getReceivedBy() {
        return receivedBy;
    }

    public void setReceivedBy(User receivedBy) {
        this.receivedBy = receivedBy;
    }

    public Travel getTravelToRate() {
        return travelToRate;
    }

    public void setTravelToRate(Travel travelToRate) {
        this.travelToRate = travelToRate;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }
}
