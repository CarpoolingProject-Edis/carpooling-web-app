package com.carpooling.main.service.interfaces;

import com.carpooling.main.model.User;
import com.carpooling.main.model.Car;

import java.util.List;

public interface UserService {

    User getById(int id);

    User getByPhoneNumber(String phoneNumber);

    User getByEmail(String email);

    List<User> getTop10UsersWithHighestRating();

    List<User> getUsersWithHighestDriverRating(int limit);

    List<User> getUsersWithHighestPassengerRating(int limit);

    void create(User user);

    void update(User user, User loggedInUser, int id);

    void updatePhoto(User user);

    void setCarToUser(User user, Car car);

    void delete(int id, User user);

    User getByUsername(String username);

    void setUserToAdmin(User user, User admin);

    void changeStatusToBlocked(User user, User admin);

    void changeStatusToActive(User user, User admin);

    void updateUserCar(User user, Car car);
}
