package com.carpooling.main.repository;


import com.carpooling.main.model.User;

public interface UserRepository {

    User getById(int id);

    User getByUsername(String username);

    User getByEmail(String email);

    void create(User user);

    void update(User user);

    void delete(int id);

}

