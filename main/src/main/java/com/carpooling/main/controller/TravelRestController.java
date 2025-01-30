package com.carpooling.main.controller;


import com.carpooling.main.exceptions.*;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.helpers.mapper.FeedbackMapper;
import com.carpooling.main.helpers.mapper.TravelMapper;
import com.carpooling.main.model.*;
import com.carpooling.main.model.dto.CreateFeedbackDto;
import com.carpooling.main.model.dto.CreateTravelDto;
import com.carpooling.main.model.dto.UpdateFeedbackDto;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.service.interfaces.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("api/travels")
public class TravelRestController {
    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;
    private final UserService userService;
    private final TravelMapper travelMapper;
    private final FeedbackMapper feedbackMapper;
    private final TravelRequestService travelRequestService;
    private final FeedbackService feedbackService;
    private final FeedbackCommentService feedbackCommentService;

    @Autowired
    public TravelRestController(TravelService travelService, AuthenticationHelper authenticationHelper,
                                UserService userService,
                               TravelMapper travelMapper,
                                FeedbackMapper feedbackMapper,
                                TravelRequestService travelRequestService,
                                FeedbackService feedbackService,
                                FeedbackCommentService feedbackCommentService) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.travelMapper = travelMapper;
        this.feedbackMapper = feedbackMapper;
        this.travelRequestService = travelRequestService;
        this.feedbackService = feedbackService;
        this.feedbackCommentService = feedbackCommentService;
    }


    @GetMapping("/{id}")
    public Travel getById(@PathVariable int id) {
        try {
            return travelService.getTravelById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }


    @PostMapping
    public Travel create(@RequestHeader HttpHeaders headers, @Valid @RequestBody CreateTravelDto travelDTO) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelMapper.fromDTO(travelDTO, user);
            travelService.create(travel);
            return travel;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{travelId}/apply")
    public String applyForTravel(@RequestHeader HttpHeaders headers, @PathVariable int travelId) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            travelRequestService.getByApplicant(loggedInUser);
            Travel travel = travelService.getTravelById(travelId);
            TravelRequest travelRequest = new TravelRequest();
            travelRequestService.create(travelRequest, loggedInUser, travel);
            return "You have successfully applied for this trip!";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (AuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TravelFinishedException | AlreadyAppliedException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{travelId}/users/{userId}/feedback")
    public <T> T createFeedbackForPassenger(@RequestHeader HttpHeaders headers,
                                            @PathVariable int travelId,
                                            @PathVariable int userId,
                                            @Valid @RequestBody CreateFeedbackDto createFeedbackDTO) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getTravelById(travelId);
            User receiver = userService.getById(userId);
            performChecksForFeedbackCreation(loggedInUser, receiver, travel);
            Feedback feedback = feedbackMapper.fromDTO(userId, loggedInUser, travel, createFeedbackDTO);
            return createFeedbackGeneric(createFeedbackDTO, receiver, feedback);
        } catch (AuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TravelNotCompletedException |
                 SelfFeedbackException |
                 AlreadyGivenFeedbackException |
                 NotPartOfTravelException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{travelId}/driver/feedback")
    public <T> T createFeedbackForDriver(@RequestHeader HttpHeaders headers,
                                         @PathVariable int travelId,
                                         @Valid @RequestBody CreateFeedbackDto createFeedbackDto) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getTravelById(travelId);
            User driver = travel.getDriver();
            performChecksForFeedbackCreation(loggedInUser, driver, travel);
            Feedback feedback = feedbackMapper.fromDTO(driver.getId(), loggedInUser, travel, createFeedbackDto);
            return createFeedbackGeneric(createFeedbackDto, driver, feedback);
        } catch (AuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (TravelNotCompletedException |
                 SelfFeedbackException |
                 AlreadyGivenFeedbackException |
                 NotPartOfTravelException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{travelId}/passengers/{passengerId}/feedback")
    public <T> T updateFeedbackForPassenger(@RequestHeader HttpHeaders headers,
                                            @PathVariable int travelId,
                                            @PathVariable int passengerId,
                                            @Valid @RequestBody UpdateFeedbackDto updateFeedbackDTO) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getTravelById(travelId);
            User receiver = userService.getById(passengerId);
            performChecksForFeedbackUpdating(loggedInUser, receiver, travel);
            return feedbackService.updateFeedbackGeneric(updateFeedbackDTO, loggedInUser, receiver);
        } catch (AuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SelfFeedbackException | NotPartOfTravelException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PutMapping("/{travelId}/driver/feedback")
    public <T> T updateFeedbackForDriver(@RequestHeader HttpHeaders headers,
                                         @PathVariable int travelId,
                                         @Valid @RequestBody UpdateFeedbackDto updateFeedbackDto) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getTravelById(travelId);
            User receiver = travel.getDriver();
            performChecksForFeedbackUpdating(loggedInUser, receiver, travel);
            return feedbackService.updateFeedbackGeneric(updateFeedbackDto, loggedInUser, receiver);
        } catch (AuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (SelfFeedbackException | NotPartOfTravelException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/{travelId}/passengers/{passengerId}/feedback")
    public String deleteFeedbackForPassenger(@RequestHeader HttpHeaders headers,
                                             @PathVariable int travelId,
                                             @PathVariable int passengerId) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getTravelById(travelId);
            User receiver = userService.getById(passengerId);
            performChecksForFeedbackDeleting(loggedInUser, receiver, travel);
            feedbackService.deleteFeedback(loggedInUser, receiver);
            return String.format("Your feedback for %s has been removed successfully.", receiver.getUsername());
        } catch (AuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (NotPartOfTravelException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @DeleteMapping("/{travelId}/driver/feedback")
    public String deleteFeedbackForDriver(@RequestHeader HttpHeaders headers,
                                          @PathVariable int travelId) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            Travel travel = travelService.getTravelById(travelId);
            User receiver = travel.getDriver();
            performChecksForFeedbackDeleting(loggedInUser, receiver, travel);
            feedbackService.deleteFeedback(loggedInUser, receiver);
            return String.format("Your feedback for %s has been removed successfully.", receiver.getUsername());
        } catch (AuthenticationFailedException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (NotPartOfTravelException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    private void performChecksForFeedbackCreation(User loggedInUser, User user, Travel travel) {
        checkIfSameUser(loggedInUser, user.getId());
        checkIfPartOfTravel(travel, loggedInUser);
        checkIfCompleted(travel);
        checkIfAlreadyGivenFeedback(loggedInUser, user);
    }

    private void performChecksForFeedbackUpdating(User loggedInUser, User user, Travel travel) {
        checkIfSameUser(loggedInUser, user.getId());
        checkIfPartOfTravel(travel, loggedInUser);
        checkIfFeedbackExists(loggedInUser, user);
    }

    private void performChecksForFeedbackDeleting(User loggedInUser, User user, Travel travel) {
        performChecksForFeedbackUpdating(loggedInUser, user, travel);
    }

    private void checkIfCompleted(Travel travel) {
        if (!travel.getTravelStatus().equals(TravelStatus.FINISHED)) {
            throw new TravelNotCompletedException("You can only rate people from completed travel.");
        }
    }

    private void checkIfPartOfTravel(Travel travel, User loggedInUser) {
        if (!(travel.getPassengers().contains(loggedInUser) ||
                travel.getDriver().getId() == loggedInUser.getId())) {
            throw new NotPartOfTravelException("You are not part of this travel.");
        }
    }

    private void checkIfSameUser(User loggedInUser, int userId) {
        if (loggedInUser.getId() == userId) {
            throw new SelfFeedbackException("You cannot give yourself feedback.");
        }
    }

    private void checkIfAlreadyGivenFeedback(User loggedInUser, User receiver) {
        Feedback foundFeedback = feedbackService.getFeedbackByGiverAndReceiver(loggedInUser, receiver);
        if (!(foundFeedback == null)) {
            throw new AlreadyGivenFeedbackException("You have already given your feedback to this person.");
        }
    }

    private void checkIfFeedbackExists(User loggedInUser, User receiver) {
        Feedback foundFeedback = feedbackService.getFeedbackByGiverAndReceiver(loggedInUser, receiver);
        if (foundFeedback == null) {
            throw new EntityNotFoundException("No such feedback exists.");
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T createFeedbackGeneric(CreateFeedbackDto createFeedbackDTO, User receiver, Feedback feedback) {
        feedbackService.create(feedback);
        feedbackService.updateRating(receiver);
        if (createFeedbackDTO.getComment() != null && !createFeedbackDTO.getComment().isEmpty()) {
            FeedbackComment feedbackComment = feedbackMapper.fromDTO(feedback, createFeedbackDTO);
            feedbackCommentService.create(feedbackComment);
            return (T) feedbackComment;
        }
        return (T) feedback;
    }
}