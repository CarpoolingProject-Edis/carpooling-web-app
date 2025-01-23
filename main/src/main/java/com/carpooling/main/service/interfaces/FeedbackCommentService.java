package com.carpooling.main.service.interfaces;


import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.FeedbackComment;
import com.carpooling.main.model.User;

import java.util.List;

public interface FeedbackCommentService {

    List<FeedbackComment> getAll();

    List<FeedbackComment> getCommentsByReceiver(User receiver);

    FeedbackComment getById(int id);

    FeedbackComment getCommentByFeedback(Feedback feedback);

    void create(FeedbackComment feedbackComment);

    void update(FeedbackComment feedbackComment);

    void delete(FeedbackComment feedbackComment);
}
