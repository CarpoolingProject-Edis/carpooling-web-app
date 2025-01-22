package com.carpooling.main.model.dto;


import jakarta.validation.constraints.NotNull;

public class CreateFeedbackDto {

    @NotNull(message = "Please rate this user.")
    private int rating;
    private String comment;

    public CreateFeedbackDto() {
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
