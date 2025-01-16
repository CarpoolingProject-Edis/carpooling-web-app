package com.carpooling.main.service;

import com.carpooling.main.exceptions.UserNotFoundException;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.UserRepository;
import com.carpooling.main.service.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User getUserByUsername(String username) {
        logger.info("Fetching user by username: {}", username);
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.error("User not found with username: {}", username);
                    return new UserNotFoundException("User not found with username: " + username);
                });
    }

    @Override
    public User saveUser(User user) {
        if (user == null || user.getUsername() == null || user.getEmail() == null) {
            logger.error("Invalid user data: {}", user);
            throw new IllegalArgumentException("User details cannot be null");
        }
        logger.info("Saving user: {}", user);
        return userRepository.save(user);
    }

    @Override
    public User getUserByEmail(String email) {
        logger.info("Fetching user by email: {}", email);
        return userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    logger.error("User not found with email: {}", email);
                    return new UserNotFoundException("User not found with email: " + email);
                });
    }
}
