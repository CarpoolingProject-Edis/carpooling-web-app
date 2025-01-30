package com.carpooling.main.helpers.mapper;


import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.FeedbackComment;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.CreateFeedbackDto;
import com.carpooling.main.repository.interfaces.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FeedbackMapper {

    private final UserRepository userRepository;

    @Autowired
    public FeedbackMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Feedback fromDTO(int receiverId, User giver, Travel travelToRate, CreateFeedbackDto createFeedbackDTO) {
        Feedback feedback = new Feedback();
        User receiver = userRepository.getById(receiverId);
        feedback.setTravelToRate(travelToRate);
        feedback.setCreatedBy(giver);
        feedback.setReceivedBy(receiver);
        feedback.setRating(createFeedbackDTO.getRating());

        return feedback;
    }

    public FeedbackComment fromDTO(Feedback feedback, CreateFeedbackDto createFeedbackDto) {
        FeedbackComment feedbackComment = new FeedbackComment();
        feedbackComment.setComment(createFeedbackDto.getComment());
        feedbackComment.setFeedback(feedback);
        return feedbackComment;
    }

}
