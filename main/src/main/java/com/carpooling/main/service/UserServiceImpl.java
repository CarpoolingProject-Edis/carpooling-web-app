package com.carpooling.main.service;


import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.exceptions.UnauthorizedOperationException;
import com.carpooling.main.model.Car;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.interfaces.TravelRepository;
import com.carpooling.main.repository.interfaces.UserRepository;
import com.carpooling.main.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final TravelRepository travelRepository;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, TravelRepository travelRepository) {
        this.userRepository = userRepository;
        this.travelRepository = travelRepository;
    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        return userRepository.getByPhoneNumber(phoneNumber);
    }

    @Override
    public User getByEmail(String email) {
        return userRepository.getByEmail(email);
    }

    @Override
    public User getByUsername(String username) {
        return userRepository.getByUsername(username);
    }

    @Override
    public void setUserToAdmin(User user, User admin) {
        checkStatusIsAdmin(admin);
        userRepository.setUserToAdmin(user);
    }

    @Override
    public List<Map<String, Object>> getAllUsers() {
        return userRepository.getAllUsers();  // ✅ Fetch users from database
    }

    @Override
    public void changeStatusToBlocked(User user, User userAuthorization) {
        checkAdminOrCreator(user, userAuthorization);
        userRepository.changeStatusToBlocked(user);
    }

    @Override
    public void changeStatusToActive(User user, User admin) {
        checkStatusIsAdmin(admin);
        userRepository.changeStatusToActive(user);
    }

    @Override
    public void create(User user) {
        boolean duplicateUsernameExists = true;
        try {
            userRepository.getByUsername(user.getUsername());
        } catch (EntityNotFoundException e) {
            duplicateUsernameExists = false;
        }
        if (duplicateUsernameExists) {
            throw new EntityDuplicateException("User", "username", user.getUsername());
        }

        boolean duplicateEmailExists = true;
        try {
            userRepository.getByEmail(user.getEmail());
        } catch (EntityNotFoundException e) {
            duplicateEmailExists = false;
        }
        if (duplicateEmailExists) {
            throw new EntityDuplicateException("User", "email", user.getEmail());
        }

        boolean duplicatePhoneExists = true;
        try {
            userRepository.getByPhoneNumber(user.getPhoneNumber());
        } catch (EntityNotFoundException e) {
            duplicatePhoneExists = false;
        }
        if (duplicatePhoneExists) {
            throw new EntityDuplicateException("User", "phone", user.getPhoneNumber());
        }

        userRepository.create(user);

    }

    @Override
    public void update(User user, User loggedInUser, int id) {
        checkAdminOrCreator(user, loggedInUser);
        userRepository.update(user);
    }

    @Override
    public void updatePhoto(User user) {
        userRepository.update(user);
    }

    @Override
    public void delete(int id, User user) {
        checkStatusIsAdmin(user);
        userRepository.delete(id);
    }

    public void setCarToUser(User user, Car car) {
        user.setCar(car);
        userRepository.update(user);
    }

    private void checkStatusIsAdmin(User admin) {
        if (!admin.getUserRole().toString().equals("Admin"))
            throw new UnauthorizedOperationException("Only admins or active creators can modify!");
    }

    public void checkAdminOrCreator(User user, User userAuthorization) {
        if (!(userAuthorization.getUserRole().toString().equals("Admin")
                || userAuthorization.getUsername().equals(user.getUsername()))) {
            throw new UnauthorizedOperationException("Only admins or active creators can modify!");
        }
    }

    @Override
    public void updateUserCar(User user, Car car) {
        user.setCar(car);
    }

    @Override
    public List<User> getTop10UsersWithHighestRating() {
        return userRepository.getUsersWithHighestRating(10);
    }

    @Override
    public List<User> getUsersWithHighestDriverRating(int limit) {
        List<Travel> travels = travelRepository.getAllTravels();
        return travels.stream()
                .map(Travel::getDriver)
                .distinct()
                .sorted(Comparator.comparingDouble(User::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }

    @Override
    public List<User> getUsersWithHighestPassengerRating(int limit) {
        return travelRepository.getAllTravels()
                .stream()
                .flatMap(travel -> travel.getPassengers().stream())
                .distinct()
                .sorted(Comparator.comparingDouble(User::getRating).reversed())
                .limit(limit)
                .collect(Collectors.toList());
    }
}
