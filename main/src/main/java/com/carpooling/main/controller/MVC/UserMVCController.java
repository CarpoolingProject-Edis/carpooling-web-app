package com.carpooling.main.controller.MVC;

import com.carpooling.main.exceptions.*;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.helpers.mapper.UserMapper;
import com.carpooling.main.model.*;
import com.carpooling.main.model.dto.UpdateFeedbackDto;
import com.carpooling.main.model.dto.UpdateUserDto;
import com.carpooling.main.model.dto.UpdateUserPasswordDto;
import com.carpooling.main.model.enums.UserRole;
import com.carpooling.main.service.interfaces.*;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserMVCController {
    private final UserService userService;

    private final TravelService travelService;
    private final TravelRequestService travelRequestService;
    private final FeedbackService feedbackService;
    private final FeedbackCommentService feedbackCommentService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;

    public UserMVCController(UserService userService,

                             TravelService travelService,
                             TravelRequestService travelRequestService,
                             FeedbackService feedbackService,
                             FeedbackCommentService feedbackCommentService,
                             AuthenticationHelper authenticationHelper,
                             UserMapper userMapper) {
        this.userService = userService;
        this.travelService = travelService;
        this.travelRequestService = travelRequestService;
        this.feedbackService = feedbackService;
        this.feedbackCommentService = feedbackCommentService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            return currentUser.getUserRole().equals(UserRole.ADMIN);
        } catch (AuthenticationFailedException e) {
            return false;
        }
    }

    @GetMapping("/profile")
    public String showProfile(HttpSession session, Model model) {
        try {
            User loggedUser = authenticationHelper.tryGetUser(session);
            List<FeedbackComment> feedbacks = feedbackService.getUniqueFeedbacks(loggedUser);
            model.addAttribute("feedbacksForUser", feedbacks);
            model.addAttribute("user", loggedUser);
            return "ProfileView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }
    }
    @GetMapping("/profile/{id}")
    public String showProfilePage(@PathVariable int id, Model model) {
        User user = userService.getById(id);
        model.addAttribute("user", user);
        return "ProfileView";
    }


    @GetMapping("/{id}")
    public String showSingleUser(@PathVariable int id, Model model, HttpSession session) {
        try {
            User viewedUser = userService.getById(id);
            User loggedUser = authenticationHelper.tryGetUser(session);
            List<FeedbackComment> feedbacks = feedbackCommentService.getCommentsByReceiver(viewedUser);
            boolean feedbackExists = feedbackService.feedbackByGiverAndReceiverExists(loggedUser, viewedUser);
            List<Feedback> totalFeedbacksForUser = feedbackService.getFeedbacksByReceiver(viewedUser);
            int feedbacksCount = 0;
            if (totalFeedbacksForUser != null) {
                feedbacksCount = totalFeedbacksForUser.size();
            }
            model.addAttribute("feedbacksForUser", feedbacks);
            model.addAttribute("user", viewedUser);
            model.addAttribute("loggedUser", loggedUser);
            model.addAttribute("feedbackExists", feedbackExists);
            model.addAttribute("numberOfPeopleWhoRated", feedbacksCount);
            return "ProfileView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{id}/update/password")
    public String showUpdatePasswordPage(@PathVariable int id, HttpSession session, Model model) {
        User currentUser = userService.getById(id);
        try {
            User logUser = authenticationHelper.tryGetUser(session);
            if (!(logUser.getUserRole().toString().equals("Admin") || logUser.getUsername().equals(currentUser.getUsername()))) {
                model.addAttribute("error", "No access");
                return "AccessDenied";
            }
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }
        UpdateUserPasswordDto dto = userMapper.toDto(currentUser);
        model.addAttribute("user", dto); // Ensure this is added to the model
        return "UpdatePasswordView";
    }


    @PostMapping("/{id}/update/password")
    public String updatePasswordUser(@PathVariable int id,
                                     @Valid @ModelAttribute("user") UpdateUserPasswordDto userDto,
                                     BindingResult bindingResult,
                                     HttpSession session,
                                     Model model) {
        User logUser = authenticationHelper.tryGetUser(session);
        User userToUpdate = userService.getById(id);
        try {
            if (!(logUser.getUserRole().equals(UserRole.ADMIN) || logUser.getUsername().equals(userToUpdate.getUsername()))) {
                model.addAttribute("error", "No access");
                return "AccessDenied";
            }
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "UpdatePasswordView";
        }


        if (!userDto.getPassword().equals(userToUpdate.getPassword())) {
            bindingResult.rejectValue("password", "password_error", "Passwords must match!");
            return "UpdatePasswordView";
        }


        if (!userDto.getChangePassword().equals(userDto.getChangePasswordConfirm())) {
            bindingResult.rejectValue("changePasswordConfirm", "password_error", "Password confirmation should match password.");
            return "UpdatePasswordView";
        }

        try {
            User user = userMapper.fromDto(id, userDto);

            userService.update(user, logUser, id);
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "UpdatePasswordView";
        }
    }

    @GetMapping("/{id}/update/profile")
    public String showUpdatePage(@PathVariable int id, HttpSession session, Model model) {
        try {
            User logUser = authenticationHelper.tryGetUser(session);
            User currentUser = userService.getById(id);

            if (!(logUser.getUserRole() == UserRole.ADMIN || logUser.getUsername().equals(currentUser.getUsername()))) {
                model.addAttribute("error", "No access");
                return "AccessDenied";
            }

            UpdateUserDto userDto = new UpdateUserDto();
            userDto.setFirstName(currentUser.getFirstName());
            userDto.setLastName(currentUser.getLastName());
            userDto.setEmail(currentUser.getEmail());
            userDto.setUsername(currentUser.getUsername());
            userDto.setPhoneNumber(currentUser.getPhoneNumber());

            model.addAttribute("updateUserDto", userDto);
            model.addAttribute("user", currentUser);

            return "UpdateProfileView";  // ✅ Ensure this matches the Thymeleaf file name
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }
    }

    @PostMapping("/{id}/update/profile")
    public String updateUser(@PathVariable int id,
                             @Valid @ModelAttribute("user") UpdateUserDto userDto,
                             BindingResult bindingResult,
                             HttpSession session,
                             Model model) {
        User logUser = authenticationHelper.tryGetUser(session);
        User userToUpdate = userService.getById(id);
        try {
            if (!(logUser.getUserRole().equals(UserRole.ADMIN) || logUser.getUsername().equals(userToUpdate.getUsername()))) {
                model.addAttribute("error", "No access");
                return "AccessDenied";
            }
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }

        if (bindingResult.hasErrors()) {
            return "UpdateProfileView";
        }

        try {
            User user = userMapper.fromDto(id, userDto);
            userService.update(user, logUser, id);
            return "redirect:/users/" + user.getId();
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username_error", e.getMessage());
            return "UpdateProfileView";
        }
    }



    @GetMapping("/{id}/admin")
    public String changeRoleToAdmin(@PathVariable int id, HttpSession session, Model model) {
        User currentUser = userService.getById(id);
        User logUser = authenticationHelper.tryGetUser(session);
        try {
            if (!(logUser.getUserRole().equals(UserRole.ADMIN))) {
                model.addAttribute("error", "No access");
                return "AccessDenied";
            }
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.setUserToAdmin(currentUser, logUser);
            return "redirect:/users/" + id;
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{id}/block")
    public String changeStatusBlock(@PathVariable int id, HttpSession session, Model model) {
        User currentUser = userService.getById(id);
        User logUser = authenticationHelper.tryGetUser(session);
        try {
            if (!(logUser.getUserRole().equals(UserRole.ADMIN) || logUser.getUsername().equals(currentUser.getUsername()))) {
                model.addAttribute("error", "No access");
                return "AccessDenied";
            }
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.changeStatusToBlocked(currentUser, logUser);
            if (logUser.getUserRole().equals(UserRole.ADMIN)) {
                return "redirect:/users";
            } else {
                return "redirect:/auth/login";
            }
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{id}/active")
    public String changeStatusActive(@PathVariable int id, HttpSession session, Model model) {
        User currentUser = userService.getById(id);
        User logUser = authenticationHelper.tryGetUser(session);
        try {
            if (!logUser.getUserRole().equals(UserRole.ADMIN)) {
                model.addAttribute("error", "No access");
                return "AccessDenied";
            }
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        }

        try {
            userService.changeStatusToActive(currentUser, logUser);
            return "redirect:/users";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{id}/upload-photo")
    public String showUploadPhotoForm(@PathVariable int id, Model model) {
        model.addAttribute("userId", id);
        return "UploadUserPhotoView";
    }


    @GetMapping("/{id}/applications")
    public String showApplications(@PathVariable int id, Model model, HttpSession session) {
        try {
            User singleUser = userService.getById(id);
            User loggedInUser = authenticationHelper.tryGetUser(session);
            List<TravelRequest> travelRequests = travelRequestService.getTravelRequestsByDriver(singleUser);
            model.addAttribute("user", singleUser);
            model.addAttribute("loggedUser", loggedInUser);
            model.addAttribute("messages", travelRequests);
            return "ApplicationUserView";
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @PostMapping("/{userId}/organized-travels/{travelId}/applications/{applicationId}/approve")
    public String approveApplication(@PathVariable int userId,
                                     @PathVariable int travelId,
                                     @PathVariable int applicationId,
                                     HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            checkIfOwner(userId, loggedInUser);

            Travel travel = travelService.getTravelById(travelId);
            TravelRequest travelRequest = travelRequestService.getById(applicationId);

            travelService.setApplicationToApproved(travel, travelRequest);
            travelRequestService.update(travelRequest);

            return "redirect:/users/" + loggedInUser.getId() + "/applications";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NoFreeSpotsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping("/{userId}/organized-travels/{travelId}/applications/{applicationId}/decline")
    public String declineApplication(@PathVariable int userId,
                                     @PathVariable int travelId,
                                     @PathVariable int applicationId,
                                     HttpSession session) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            checkIfOwner(userId, loggedInUser);

            Travel travel = travelService.getTravelById(travelId);
            TravelRequest travelRequest = travelRequestService.getById(applicationId);


            travelService.setApplicationToDeclined(travel, travelRequest);
            travelRequestService.update(travelRequest);

            return "redirect:/users/" + loggedInUser.getId() + "/applications";
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NoFreeSpotsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @GetMapping("/{userId}/update-feedback")
    public String showUpdateFeedbackForViewedUser(@PathVariable int userId,
                                                  Model model,
                                                  HttpSession session) {

        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            User receiver = userService.getById(userId);
            Feedback existingFeedback = feedbackService.getFeedbackByGiverAndReceiver(loggedInUser, receiver);
            FeedbackComment existingComment = feedbackCommentService.getCommentByFeedback(existingFeedback);
            boolean commentExists = existingComment != null;
            model.addAttribute("isAuthor", true);
            model.addAttribute("receiver", receiver);
            model.addAttribute("feedback", new UpdateFeedbackDto());
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

    @PostMapping("/{userId}/update-feedback")
    public String handleUpdateFeedbackForViewedUser(@PathVariable int userId,
                                                    Model model,
                                                    HttpSession session,
                                                    @Valid @ModelAttribute UpdateFeedbackDto feedbackDTO) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            User receiver = userService.getById(userId);
            feedbackService.updateFeedback(feedbackDTO, loggedInUser, receiver);
            return "redirect:/users/" + receiver.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    @GetMapping("/{userId}/remove-feedback")
    public String removeFeedbackForViewedUser(@PathVariable int userId,
                                              HttpSession session,
                                              Model model) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(session);
            User viewedUser = userService.getById(userId);
            feedbackService.deleteFeedback(loggedInUser, viewedUser);
            return "redirect:/users/" + viewedUser.getId();
        } catch (AuthenticationFailedException e) {
            return "redirect:/auth/login";
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "NotFoundView";
        }
    }

    private static void checkIfOwner(int targetUserId, User executingUser) {
        if (targetUserId != executingUser.getId()) {
            throw new UnauthorizedOperationException("You are not authorized to do that.");
        }
    }
}
