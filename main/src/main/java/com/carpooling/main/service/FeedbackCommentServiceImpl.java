package com.carpooling.main.service;

import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.FeedbackComment;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.interfaces.FeedbackCommentRepository;
import com.carpooling.main.service.interfaces.FeedbackCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FeedbackCommentServiceImpl implements FeedbackCommentService {

    private final FeedbackCommentRepository feedbackCommentRepository;

    @Autowired
    public FeedbackCommentServiceImpl(FeedbackCommentRepository feedbackCommentRepository) {
        this.feedbackCommentRepository = feedbackCommentRepository;
    }

    @Override
    public List<FeedbackComment> getAll() {
        return feedbackCommentRepository.getAll();
    }

    @Override
    public List<FeedbackComment> getCommentsByReceiver(User receiver) {
        return feedbackCommentRepository.getCommentsByReceiver(receiver);
    }

    @Override
    public FeedbackComment getById(int id) {
        return feedbackCommentRepository.getById(id);
    }

    @Override
    public FeedbackComment getCommentByFeedback(Feedback feedback) {
        return feedbackCommentRepository.getCommentByFeedback(feedback);
    }

    @Override
    public void create(FeedbackComment feedbackComment) {
        feedbackCommentRepository.create(feedbackComment);
    }

    @Override
    public void update(FeedbackComment feedbackComment) {
        feedbackCommentRepository.update(feedbackComment);
    }

    @Override
    public void delete(FeedbackComment feedbackComment) {
        feedbackCommentRepository.delete(feedbackComment);
    }
}
