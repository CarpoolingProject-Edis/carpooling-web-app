package com.carpooling.main.repository.interfaces;


import com.carpooling.main.model.User;

import java.util.List;

public interface UserRepository {
    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    User getByPhoneNumber(String phoneNumber);

    void create(User user);

    void update(User user);

    void delete(int id);

    void setUserToAdmin(User user);

    void changeStatusToBlocked(User user);

    void changeStatusToActive(User user);

    List<User> getUsersWithHighestRating(int limit);

}
