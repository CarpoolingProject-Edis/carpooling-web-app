package com.carpooling.main.service.interfaces;

import com.carpooling.main.model.User;

import java.util.Optional;

public interface UserService {

    User getUserByUsername(String username);

    User saveUser(User user);

    Optional<User> getUserByEmail(String email);
}
