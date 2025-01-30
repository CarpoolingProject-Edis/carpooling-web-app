package com.carpooling.main.model.dto;

import java.util.OptionalInt;

public class UpdateFeedbackDto {

    private OptionalInt rating;
    private String comment;

    public UpdateFeedbackDto() {
    }

    public OptionalInt getRating() {
        return rating;
    }

    public void setRating(OptionalInt rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
