package com.carpooling.main.service.interfaces;

import com.carpooling.main.model.User;

public interface UserService {

    User getUserByUsername(String username);

    User saveUser(User user);

    User getUserByEmail(String email);

    boolean existsByUsername(String username);
}
