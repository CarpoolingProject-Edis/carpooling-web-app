package com.carpooling.main.controller;


import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.exceptions.NoFreeSpotsException;
import com.carpooling.main.exceptions.UnauthorizedOperationException;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.helpers.mapper.CarMapper;
import com.carpooling.main.helpers.mapper.UserMapper;
import com.carpooling.main.model.Car;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.CarDto;
import com.carpooling.main.model.dto.UpdateUserDto;
import com.carpooling.main.model.enums.ApplicationStatus;
import com.carpooling.main.model.enums.UserRole;
import com.carpooling.main.service.interfaces.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    private final UserService userService;
    private final TravelService travelService;
    private final TravelRequestService travelRequestService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final CarMapper carMapper;
    private final CarService carService;
    private final FeedbackService feedbackService;

    @Autowired
    public UserRestController(UserService userService,
                              TravelService travelService,
                              TravelRequestService travelRequestService,
                              AuthenticationHelper authenticationHelper,
                              UserMapper userMapper,
                              CarMapper carMapper,
                              CarService carService,
                              FeedbackService feedbackService) {
        this.userService = userService;
        this.travelService = travelService;
        this.travelRequestService = travelRequestService;
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.carMapper = carMapper;
        this.carService = carService;
        this.feedbackService = feedbackService;
    }
    @GetMapping("/{id}")
    public User getById(@PathVariable int id) {
        try {
            return userService.getById(id);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{id}/feedbacks")
    public <T> List<T> getFeedbacksByReceiver(@PathVariable int id) {
        try {
            User receiver = userService.getById(id);
            return feedbackService.getUniqueFeedbacks(receiver);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/{userId}/organized-travels/{travelId}/applications/{applicationId}")
    public TravelRequest viewApplication(@RequestHeader HttpHeaders headers,
                                         @PathVariable int userId,
                                         @PathVariable int travelId,
                                         @PathVariable int applicationId) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            checkIfOwner(userId, loggedInUser);
            return travelRequestService.getById(applicationId);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (UnauthorizedOperationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        } catch (NoFreeSpotsException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        try {
            userService.create(user);
            return user;
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/update/profile")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, loggedInUser);
            userService.update(loggedInUser, loggedInUser, id);
            return loggedInUser;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/update/car")
    public User updateCar(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody CarDto carDTO) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, loggedInUser);
            Car car = carMapper.fromDto(carDTO);
            carService.create(car);
            userService.setCarToUser(loggedInUser, car);
            return loggedInUser;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }

    @PutMapping("/{id}/update/password")
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UpdateUserDto userDto) {
        try {
            User loggedInUser = authenticationHelper.tryGetUser(headers);
            checkAccessPermissions(id, loggedInUser);
            loggedInUser = userMapper.fromDto(id, userDto);
            userService.update(loggedInUser, loggedInUser, id);
            return loggedInUser;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (EntityDuplicateException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
        }
    }
//
//
//    @PutMapping("/{id}/set-admin}")
//    public void changeRoleToAdmin(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            User userToAdmin = userService.getById(id);
//            User userAuthorization = authenticationHelper.tryGetUser(headers);
//            userService.setUserToAdmin(userToAdmin, userAuthorization);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
//
//    @PutMapping("/{id}/set-active")
//    public void setActive(@RequestHeader HttpHeaders headers, @PathVariable int id) {
//        try {
//            User useActive = userService.getById(id);
//            User userAuthorization = authenticationHelper.tryGetUser(headers);
//            userService.changeStatusToActive(useActive, userAuthorization);
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        }
//    }
////
////    @PutMapping("/{id}/set-blocked}")
////    public void setBlock(@RequestHeader HttpHeaders headers, @PathVariable int id) {
////        try {
////            User useActive = userService.getById(id);
////            User userAuthorization = authenticationHelper.tryGetUser(headers);
////            userService.changeStatusToBlocked(useActive, userAuthorization);
////        } catch (EntityNotFoundException e) {
////            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
////        }
////    }
//
//    @PutMapping("/{userId}/organized-travels/{travelId}/applications/{applicationId}/approve")
//    public TravelRequest approveApplication(@RequestHeader HttpHeaders headers,
//                                            @PathVariable int userId,
//                                            @PathVariable int travelId,
//                                            @PathVariable int applicationId) {
//        try {
//            User loggedInUser = authenticationHelper.tryGetUser(headers);
//            checkIfOwner(userId, loggedInUser);
//            Travel travel = travelService.getTravelById(travelId);
//            TravelRequest travelRequest = travelRequestService.getById(applicationId);
//            travelService.setApplicationToApproved(travel, travelRequest);
//            travelRequestService.update(travelRequest);
//            return travelRequest;
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (UnauthorizedOperationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (NoFreeSpotsException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//        }
//    }
//
//    @PutMapping("/{userId}/organized-travels/{travelId}/applications/{applicationId}/decline")
//    public TravelRequest declineApplication(@RequestHeader HttpHeaders headers,
//                                            @PathVariable int userId,
//                                            @PathVariable int travelId,
//                                            @PathVariable int applicationId) {
//        try {
//            User loggedInUser = authenticationHelper.tryGetUser(headers);
//            checkIfOwner(userId, loggedInUser);
//            Travel travel = travelService.getTravelById(travelId);
//            TravelRequest travelRequest = travelRequestService.getById(applicationId);
//            if (travelRequest.getApplicationStatus().equals(ApplicationStatus.APPROVED)) {
//                travelService.setApprovedApplicationToDeclined(travel, travelRequest);
//            } else {
//                travelService.setApplicationToDeclined(travel, travelRequest);
//            }
//            travelRequestService.update(travelRequest);
//            return travelRequest;
//        } catch (EntityNotFoundException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
//        } catch (UnauthorizedOperationException e) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
//        } catch (NoFreeSpotsException e) {
//            throw new ResponseStatusException(HttpStatus.CONFLICT, e.getMessage());
//        }
//    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id, @RequestHeader HttpHeaders headers) {
        try {
            User user = authenticationHelper.tryGetUser(headers);
            userService.delete(id, user);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    private static void checkAccessPermissions(int targetUserId, User executingUser) {
        if (!executingUser.getUserRole().equals(UserRole.ADMIN) && targetUserId != executingUser.getId()) {
            throw new UnauthorizedOperationException("You are not authorized to do that.");
        }
    }

    private static void checkIfOwner(int targetUserId, User executingUser) {
        if (targetUserId != executingUser.getId()) {
            throw new UnauthorizedOperationException("You are not authorized to do that.");
        }
    }
}

