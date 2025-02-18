package com.carpooling.main.controller.REST;


import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.exceptions.NoFreeSpotsException;
import com.carpooling.main.exceptions.UnauthorizedOperationException;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.helpers.mapper.CarMapper;
import com.carpooling.main.helpers.mapper.UserMapper;
import com.carpooling.main.model.Car;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.CarDto;
import com.carpooling.main.model.dto.UpdateUserPasswordDto;
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
    private final TravelRequestService travelRequestService;
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final CarMapper carMapper;
    private final CarService carService;
    private final FeedbackService feedbackService;

    @Autowired
    public UserRestController(UserService userService,
                              TravelRequestService travelRequestService,
                              AuthenticationHelper authenticationHelper,
                              UserMapper userMapper,
                              CarMapper carMapper,
                              CarService carService,
                              FeedbackService feedbackService) {
        this.userService = userService;
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

    @GetMapping("/{userId}/organized-travels/{ignoredTravelId}/applications/{applicationId}")
    public TravelRequest viewApplication(@RequestHeader HttpHeaders headers,
                                         @PathVariable int userId,
                                         @PathVariable int applicationId, @PathVariable String ignoredTravelId) {
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
    public User update(@RequestHeader HttpHeaders headers, @PathVariable int id, @Valid @RequestBody UpdateUserPasswordDto userDto) {
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

