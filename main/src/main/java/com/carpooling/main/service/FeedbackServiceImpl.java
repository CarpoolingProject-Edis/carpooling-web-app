package com.carpooling.main.service;

import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.FeedbackRepository;
import com.carpooling.main.service.interfaces.FeedbackService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;

    public FeedbackServiceImpl(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public List<Feedback> getAllFeedbacks() {
        return feedbackRepository.findAll();
    }

    @Override
    public Optional<Feedback> getFeedbackById(Integer id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public List<Feedback> getFeedbacksByTravel(Travel travel) {
        return feedbackRepository.findByTravel(travel);
    }

    @Override
    public List<Feedback> getFeedbacksFromUser(User fromUser) {
        return feedbackRepository.findByFromUser(fromUser);
    }

    @Override
    public List<Feedback> getFeedbacksToUser(User toUser) {
        return feedbackRepository.findByToUser(toUser);
    }

    @Override
    public Feedback createOrUpdateFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(Integer id) {
        feedbackRepository.deleteById(id);
    }
}
