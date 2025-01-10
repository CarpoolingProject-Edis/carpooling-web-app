package com.carpooling.main.service;

import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.FeedbackRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    public Optional<Feedback> getFeedbackById(Integer id) {
        return feedbackRepository.findById(id);
    }

    public List<Feedback> getFeedbacksByTravel(Travel travel) {
        return feedbackRepository.findByTravel(travel);
    }

    public List<Feedback> getFeedbacksFromUser(User fromUser) {
        return feedbackRepository.findByFromUser(fromUser);
    }

    public List<Feedback> getFeedbacksToUser(User toUser) {
        return feedbackRepository.findByToUser(toUser);
    }

    public Feedback createOrUpdateFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    public void deleteFeedback(Integer id) {
        feedbackRepository.deleteById(id);
    }
}
