package com.carpooling.main.service.interfaces;

import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;

import java.util.List;
import java.util.Optional;

public interface FeedbackService {

    List<Feedback> getAllFeedbacks();

    Optional<Feedback> getFeedbackById(Integer id);

    List<Feedback> getFeedbacksByTravel(Travel travel);

    List<Feedback> getFeedbacksFromUser(User fromUser);

    List<Feedback> getFeedbacksToUser(User toUser);

    Feedback createOrUpdateFeedback(Feedback feedback);

    void deleteFeedback(Integer id);

}
