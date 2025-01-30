package com.carpooling.main.service.interfaces;

import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.UpdateFeedbackDto;

import java.util.List;

public interface FeedbackService {

    Feedback getFeedbackById(int id);

    Feedback getFeedbackByGiverAndReceiver(User giver, User receiver);

    boolean feedbackByGiverAndReceiverExists(User giver, User receiver);

    List<Feedback> getFeedbacks();

    List<Feedback> getFeedbacksByReceiver(User user);

    <T> List<T> getUniqueFeedbacks(User receiver);

    void calculateAverageRatingForUser(User user);

    void updateRating(User user);

    void create(Feedback feedback);

    <T> T updateFeedbackGeneric(UpdateFeedbackDto updateFeedbackDto, User loggedInUser, User receiver);

    void deleteFeedback(User loggedInUser, User receiver);

    void update(Feedback feedback);

    void delete(Feedback feedback);
}
