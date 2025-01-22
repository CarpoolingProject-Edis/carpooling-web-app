package com.carpooling.main.repository.interfaces;


import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.User;

import java.util.List;

public interface FeedbackRepository {

    Feedback getFeedbackById(int id);

    Feedback getFeedbackByGiverAndReceiver(User loggedInUser, User receiver);

    boolean feedbackByGiverAndReceiverExists(User giver, User receiver);

    List<Feedback> getFeedbacks();

    List<Feedback> getFeedbacksByReceiver(User receiver);

    List<Feedback> getUncommentedFeedbacksByReceiver(User receiver);

    void create(Feedback feedback);

    void update(Feedback feedback);

    void delete(Feedback feedback);
}
