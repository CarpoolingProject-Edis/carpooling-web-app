package com.carpooling.main.service;


import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.FeedbackComment;
import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.UpdateFeedbackDto;
import com.carpooling.main.repository.interfaces.FeedbackRepository;
import com.carpooling.main.repository.interfaces.UserRepository;
import com.carpooling.main.service.interfaces.FeedbackCommentService;
import com.carpooling.main.service.interfaces.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final FeedbackCommentService feedbackCommentService;
    private final UserRepository userRepository;

    @Autowired
    public FeedbackServiceImpl(FeedbackRepository feedbackRepository,
                               FeedbackCommentService feedbackCommentService,
                               UserRepository userRepository) {
        this.feedbackRepository = feedbackRepository;
        this.feedbackCommentService = feedbackCommentService;
        this.userRepository = userRepository;
    }

    @Override
    public Feedback getFeedbackById(int id) {
        return feedbackRepository.getFeedbackById(id);
    }

    @Override
    public Feedback getFeedbackByGiverAndReceiver(User loggedInUser, User receiver) {
        return feedbackRepository.getFeedbackByGiverAndReceiver(loggedInUser, receiver);
    }

    @Override
    public boolean feedbackByGiverAndReceiverExists(User giver, User receiver) {
        return feedbackRepository.feedbackByGiverAndReceiverExists(giver, receiver);
    }

    @Override
    public List<Feedback> getFeedbacks() {
        return feedbackRepository.getFeedbacks();
    }

    @Override
    public List<Feedback> getFeedbacksByReceiver(User user) {
        return feedbackRepository.getFeedbacksByReceiver(user);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> List<T> getUniqueFeedbacks(User receiver) {
        List<Feedback> uncommentedFeedbacks = feedbackRepository.getUncommentedFeedbacksByReceiver(receiver);
        List<FeedbackComment> feedbacksWithComments = feedbackCommentService.getCommentsByReceiver(receiver);
        List<T> combinedList = new ArrayList<>();
        if (uncommentedFeedbacks != null) {
            combinedList.addAll((List<T>) uncommentedFeedbacks);
        }
        if (feedbacksWithComments != null) {
            combinedList.addAll((List<T>) feedbacksWithComments);
        }
        return combinedList;
    }


    @Override
    public void deleteFeedback(User loggedInUser, User receiver) {
        Feedback foundFeedback = getFeedbackByGiverAndReceiver(loggedInUser, receiver);
        FeedbackComment feedbackComment = feedbackCommentService.getCommentByFeedback(foundFeedback);

        if (feedbackComment != null) {
            feedbackCommentService.delete(feedbackComment);
        }

        delete(foundFeedback);
        updateRating(receiver);
    }

    @Override
    public void create(Feedback feedback) {
        boolean duplicateExits = true;

        try {
            feedbackRepository.getFeedbackById(feedback.getId());
        } catch (EntityNotFoundException e) {
            duplicateExits = false;
        }

        if (duplicateExits) {
            throw new EntityDuplicateException("Feedback", "id", feedback.getId());
        }

        feedbackRepository.create(feedback);
        calculateAverageRatingForUser(feedback.getReceivedBy());
    }


    @Override
    public <T> T updateFeedbackGeneric(UpdateFeedbackDto updateFeedbackDTO, User loggedInUser, User receiver) {
        Feedback foundFeedback = getFeedbackByGiverAndReceiver(loggedInUser, receiver);

        if (updateFeedbackDTO.getRating().isPresent()) {
            foundFeedback.setRating(updateFeedbackDTO.getRating().getAsInt());
            update(foundFeedback);
            updateRating(receiver);
        }

        FeedbackComment feedbackWithComment = feedbackCommentService.getCommentByFeedback(foundFeedback);
        String commentFromDTO = updateFeedbackDTO.getComment();
        if (feedbackWithComment != null) {
            if (commentFromDTO != null) {
                feedbackWithComment.setComment(commentFromDTO);
                if (!(feedbackWithComment.getComment().isBlank())) {
                    feedbackCommentService.update(feedbackWithComment);
                    return (T) feedbackWithComment;
                }
            }
            feedbackCommentService.delete(feedbackWithComment);
        } else {
            if (commentFromDTO != null) {
                if (!commentFromDTO.isBlank()) {
                    feedbackWithComment = new FeedbackComment();
                    feedbackWithComment.setComment(commentFromDTO);
                    feedbackWithComment.setFeedback(foundFeedback);
                    feedbackCommentService.create(feedbackWithComment);
                    return (T) feedbackWithComment;
                }
            }
        }

        return (T) foundFeedback;
    }

    @Override
    public void updateRating(User user) {
        calculateAverageRatingForUser(user);
        userRepository.update(user);
    }

    @Override
    public void update(Feedback feedback) {
        feedbackRepository.update(feedback);
        calculateAverageRatingForUser(feedback.getReceivedBy());
    }

    @Override
    public void delete(Feedback feedback) {
        feedbackRepository.delete(feedback);
    }

    @Override
    public void calculateAverageRatingForUser(User user) {
        List<Feedback> userFeedbacks = getFeedbacksByReceiver(user);
        if (userFeedbacks == null) {
            user.setRating(0);
        } else {
            double totalRating = 0;
            for (Feedback feedback : userFeedbacks) {
                totalRating += feedback.getRating();
            }
            totalRating /= userFeedbacks.size();
            user.setRating(Math.round(totalRating * 10) / 10.0);
        }
    }
}
