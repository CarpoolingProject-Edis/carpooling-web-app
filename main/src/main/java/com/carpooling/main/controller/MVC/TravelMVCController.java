package com.carpooling.main.controller.MVC;


import com.carpooling.main.exceptions.*;
import com.carpooling.main.helpers.mapper.FeedbackMapper;
import com.carpooling.main.model.*;
import com.carpooling.main.model.dto.CreateFeedbackDto;
import com.carpooling.main.model.dto.CreateTravelDto;
import com.carpooling.main.model.dto.UpdateFeedbackDto;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.service.interfaces.TravelService;
import com.carpooling.main.service.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import com.carpooling.main.service.interfaces.*;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.helpers.mapper.TravelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/travels")
public class TravelMVCController {

    private final TravelService travelService;
    private final UserService userService;
    private final TravelRequestService travelRequestService;
    private final FeedbackService feedbackService;
    private final FeedbackCommentService feedbackCommentService;
    private final AuthenticationHelper authenticationHelper;
    private final TravelMapper travelMapper;
    private final FeedbackMapper feedbackMapper;

    @Autowired
    public TravelMVCController(TravelService travelService,
                               UserService userService,
                               TravelRequestService travelRequestService,
                               FeedbackService feedbackService,
                               FeedbackCommentService feedbackCommentService,
                               AuthenticationHelper authenticationHelper,
                               TravelMapper travelMapper,
                               FeedbackMapper feedbackMapper) {
        this.travelService = travelService;
        this.userService = userService;
        this.travelRequestService = travelRequestService;
        this.feedbackService = feedbackService;
        this.feedbackCommentService = feedbackCommentService;
        this.authenticationHelper = authenticationHelper;
        this.travelMapper = travelMapper;
        this.feedbackMapper = feedbackMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @GetMapping("/{id}")
    public String showSingleTravel(@PathVariable int id, Model model, HttpSession session) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(session);
            Travel singleTravel = travelService.getTravelById(id);

            // Use the updated service method that returns requests only if the logged user is the driver.
            List<TravelRequest> requestsByPopulate = travelRequestService.getTravelRequestsByPopulate(loggedUser, id);
            boolean hasApplied = !requestsByPopulate.isEmpty();
            boolean isDriver = loggedUser.getId() == singleTravel.getDriver().getId();
            boolean isPartOfTravel = singleTravel.getPassengers().contains(loggedUser);
            boolean isCancelled = singleTravel.getTravelStatus().equals(TravelStatus.CANCELLED);

            model.addAttribute("isCancelled", isCancelled);
            model.addAttribute("isPartOfTravel", isPartOfTravel);
            model.addAttribute("isDriver", isDriver);
            model.addAttribute("hasApplied", hasApplied);
            model.addAttribute("travel", singleTravel);
            model.addAttribute("logUser", loggedUser);
            model.addAttribute("requests", requestsByPopulate);
            return "SingleTravelView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{travelId}/requests/{requestId}/accept")
    public String acceptRequest(@PathVariable int travelId, @PathVariable int requestId, HttpSession session) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(session);
            travelRequestService.acceptRequest(loggedUser, travelId, requestId);
            return "redirect:/travels/" + travelId;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @PostMapping("/{travelId}/requests/{requestId}/reject")
    public String rejectRequest(@PathVariable int travelId, @PathVariable int requestId, HttpSession session) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(session);
            travelRequestService.rejectRequest(loggedUser, travelId, requestId);
            return "redirect:/travels/" + travelId;
        } catch (Exception e) {
            return "redirect:/error";
        }
    }

    @GetMapping("/user")
    public String viewAllTravelsForUser(HttpSession session, Model model) {
        User user;
        try {
            user = authenticationHelper.tryGetUser(session);
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }
        List<Travel> travels = new ArrayList<>(travelService.getTravelsByUser(user));
        User logUser = authenticationHelper.tryGetUser(session);
        model.addAttribute("travels", travels);
        model.addAttribute("logUser", logUser);
        return "UserTravelsView";
    }

    @GetMapping("/new")
    public String showCreateTravel(HttpSession session, Model model) {
        try {
            authenticationHelper.tryGetUser(session);
            model.addAttribute("travel", new CreateTravelDto());
            return "CreateTravelView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/new")
    public String handleCreateTravel(@Valid @ModelAttribute("travel") CreateTravelDto travelDTO,
                                     BindingResult bindingResult,
                                     Model model,
                                     HttpSession session) {
        try {
            User user = authenticationHelper.tryGetUser(session);
            if (bindingResult.hasErrors()) {
                return "CreateTravelView";
            }
            Travel travel = travelMapper.fromDTO(travelDTO, user);
            List<Travel> ongoingUserTravels = travelService.getOngoingUserTravels(user, travel);
            if (!ongoingUserTravels.isEmpty()) {
                return "CreateTravelView";
            }
            travelService.create(travel);
            return "redirect:/travels/" + travel.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (InvalidLocationException e) {
            bindingResult.rejectValue("startingPoint", "location_error", e.getMessage());
            bindingResult.rejectValue("destination", "location_error", e.getMessage());
            return "CreateTravel";
        }
    }

    @GetMapping("/{id}/cancel")
    public String cancelTravel(@PathVariable("id") int id, HttpSession session, Model model) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            Travel travel = travelService.getTravelById(id);
            travelService.changeStatusToCancelled(loggedInUser, travel);
            return "redirect:/travels/" + travel.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (UnauthorizedOperationException e) {
            model.addAttribute("error", e.getMessage());
        }
        return "redirect:/travels/user";
    }

    @PostMapping("/{travelId}/apply")
    public String applyForTravel(@PathVariable int travelId, HttpSession session, Model model) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            Travel travel = travelService.getTravelById(travelId);
            TravelRequest travelRequest = new TravelRequest();
            travelRequestService.create(travelRequest, loggedInUser, travel);
            model.addAttribute("logUser", loggedInUser);

            return "redirect:/travels/ApplySuccessView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (YouAreTheDriverException | TravelFinishedException | AlreadyAppliedException e) {
            model.addAttribute("error", e.getMessage());
            return "ConflictRequestView";
        }
    }

    // Success Page Route
    @GetMapping("/ApplySuccessView")
    public String showApplySuccessPage() {
        return "ApplySuccessView";
    }


    @PostMapping("/{travelId}/withdraw")
    public String withdrawFromTravel(@PathVariable int travelId, HttpSession session, Model model) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            Travel travel = travelService.getTravelById(travelId);
            TravelRequest travelRequest = travelRequestService.getByApplicantAndTravel(loggedInUser, travel);
            travelRequestService.delete(travelRequest);
            model.addAttribute("loggedUser", loggedInUser);
            return "redirect:/travels/" + travel.getId();
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (YouAreTheDriverException | TravelFinishedException e) {
            model.addAttribute("error", e.getMessage());
            return "ConflictRequestView";
        }
    }

    @GetMapping("/{travelId}/driver/new-feedback")
    public String showCreateFeedbackForDriver(@PathVariable int travelId, Model model, HttpSession session) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(session);
            Travel travel = travelService.getTravelById(travelId);
            User driver = travel.getDriver();
            if (feedbackService.feedbackByGiverAndReceiverExists(loggedUser, driver)) {
                return String.format("redirect:/travels/%d/driver/update-feedback", travelId);
            }
            model.addAttribute("travel", travel);
            model.addAttribute("receiver", driver);
            model.addAttribute("feedback", new CreateFeedbackDto());
            model.addAttribute("isPassenger", true);
            return "CreateFeedbackView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{travelId}/driver/new-feedback")
    public String handleCreateFeedbackForDriver(@PathVariable int travelId,
                                                @Valid @ModelAttribute("feedback") CreateFeedbackDto feedbackDTO,
                                                BindingResult bindingResult,
                                                Model model,
                                                HttpSession session) {
        try {
            if (bindingResult.hasErrors()) {
                return "CreateFeedbackView";
            }
            User loggedInUser = authenticationHelper.tryGetUser(session);
            Travel travel = travelService.getTravelById(travelId);
            User driver = travel.getDriver();
            Feedback feedback = feedbackMapper.fromDTO(driver.getId(), loggedInUser, travel, feedbackDTO);
            createFeedback(feedbackDTO, driver, feedback);
            return "redirect:/users/" + driver.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{travelId}/driver/update-feedback")
    public String showUpdateFeedbackForDriver(@PathVariable int travelId, Model model, HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            Travel travel = travelService.getTravelById(travelId);
            User driver = travel.getDriver();
            Feedback existingFeedback = feedbackService.getFeedbackByGiverAndReceiver(loggedInUser, driver);
            FeedbackComment existingComment = feedbackCommentService.getCommentByFeedback(existingFeedback);
            boolean commentExists = existingComment != null;
            model.addAttribute("travel", travel);
            model.addAttribute("receiver", driver);
            model.addAttribute("feedback", new UpdateFeedbackDto());
            model.addAttribute("existingFeedback", existingFeedback);
            model.addAttribute("existingComment", existingComment);
            model.addAttribute("commentExists", commentExists);
            model.addAttribute("isPassenger", true);
            return "UpdateFeedbackView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{travelId}/driver/update-feedback")
    public String handleUpdateFeedbackForDriver(@PathVariable int travelId,
                                                Model model,
                                                HttpSession session,
                                                @Valid @ModelAttribute("feedback") UpdateFeedbackDto feedbackDTO) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            Travel travel = travelService.getTravelById(travelId);
            User receiver = travel.getDriver();
            feedbackService.updateFeedback(feedbackDTO, loggedInUser, receiver);
            return "redirect:/users/" + receiver.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{travelId}/passengers/{passengerId}/new-feedback")
    public String showCreateFeedbackForPassenger(@PathVariable int travelId,
                                                 @PathVariable int passengerId,
                                                 Model model,
                                                 HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            User receiver = userService.getById(passengerId);
            if (feedbackService.feedbackByGiverAndReceiverExists(loggedInUser, receiver)) {
                return String.format("redirect:/travels/%d/passengers/%d/update-feedback", travelId, passengerId);
            }
            Travel travel = travelService.getTravelById(travelId);
            model.addAttribute("travel", travel);
            model.addAttribute("receiver", receiver);
            model.addAttribute("feedback", new CreateFeedbackDto());
            model.addAttribute("isDriver", true);
            return "CreateFeedbackView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{travelId}/passengers/{passengerId}/new-feedback")
    public String handleCreateFeedbackForPassenger(@Valid @ModelAttribute("feedback") CreateFeedbackDto feedbackDTO,
                                                   @PathVariable int travelId,
                                                   @PathVariable int passengerId,
                                                   BindingResult bindingResult,
                                                   Model model,
                                                   HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            User receiver = userService.getById(passengerId);
            Travel travel = travelService.getTravelById(travelId);
            if (bindingResult.hasErrors()) {
                return "CreateFeedbackView";
            }
            Feedback feedback = feedbackMapper.fromDTO(receiver.getId(), loggedInUser, travel, feedbackDTO);
            createFeedback(feedbackDTO, receiver, feedback);
            return "redirect:/users/" + receiver.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (SelfFeedbackException e) {
            model.addAttribute("error", e.getMessage());
            return "ConflictRequestView";
        }
    }

    @GetMapping("/{travelId}/passengers/{passengerId}/update-feedback")
    public String showUpdateFeedbackForPassenger(@PathVariable int travelId,
                                                 @PathVariable int passengerId,
                                                 Model model,
                                                 HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            User receiver = userService.getById(passengerId);
            Travel travel = travelService.getTravelById(travelId);
            Feedback existingFeedback = feedbackService.getFeedbackByGiverAndReceiver(loggedInUser, receiver);
            FeedbackComment existingComment = feedbackCommentService.getCommentByFeedback(existingFeedback);
            boolean commentExists = existingComment != null;
            model.addAttribute("travel", travel);
            model.addAttribute("receiver", receiver);
            model.addAttribute("feedback", new UpdateFeedbackDto());
            model.addAttribute("isDriver", true);
            model.addAttribute("existingFeedback", existingFeedback);
            model.addAttribute("existingComment", existingComment);
            model.addAttribute("commentExists", commentExists);
            return "UpdateFeedbackView";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{travelId}/passengers/{passengerId}/update-feedback")
    public String handleUpdateFeedbackForPassenger(@PathVariable int travelId,
                                                   @PathVariable int passengerId,
                                                   Model model,
                                                   HttpSession session,
                                                   @Valid @ModelAttribute UpdateFeedbackDto feedbackDTO) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            User receiver = userService.getById(passengerId);
            feedbackService.updateFeedback(feedbackDTO, loggedInUser, receiver);
            return "redirect:/users/" + receiver.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    private void createFeedback(CreateFeedbackDto createFeedbackDTO, User receiver, Feedback feedback) {
        feedbackService.create(feedback);
        feedbackService.updateRating(receiver);
        if (createFeedbackDTO.getComment() != null && !createFeedbackDTO.getComment().isEmpty()) {
            FeedbackComment feedbackComment = feedbackMapper.fromDTO(feedback, createFeedbackDTO);
            feedbackCommentService.create(feedbackComment);
        }
    }
}
