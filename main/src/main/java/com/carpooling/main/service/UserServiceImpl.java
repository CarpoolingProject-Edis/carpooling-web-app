package com.carpooling.main.service;

import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.UserRepository;
import com.carpooling.main.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;


    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User getById(int id) {
        return userRepository.getById(id);
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

        userRepository.create(user);

    }

    @Override
    public void update(User user, User loggedInUser, int id) {
        userRepository.update(user);
    }

    @Override
    public void delete(int id, User user) {
        userRepository.delete(id);
    }


}
