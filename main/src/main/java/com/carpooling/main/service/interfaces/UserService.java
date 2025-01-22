package com.carpooling.main.service.interfaces;


import com.carpooling.main.model.User;

public interface UserService {

    User getById(int id);

    User getByEmail(String email);

    User getByUsername(String username);

    void create(User user);

    void update(User user, User loggedInUser, int id);

    void delete(int id, User user);


}
