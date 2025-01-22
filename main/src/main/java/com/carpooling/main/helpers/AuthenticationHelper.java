package com.carpooling.main.helpers;

import com.carpooling.main.exceptions.AuthenticationFailedException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.exceptions.UnauthorizedOperationException;
import com.carpooling.main.model.User;
import com.carpooling.main.service.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;


@Component
public class AuthenticationHelper {

    private final UserService userService;

    @Autowired
    public AuthenticationHelper(UserService userService) {
        this.userService = userService;
    }

    public User tryGetUser(HttpHeaders headers) {
        if (!headers.containsKey("Authorization")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "The request resource requires authorization. ");
        }
        try {
            String userInfo = headers.getFirst("Authorization");
            String username = getUsername(userInfo);
            String password = getPassword(userInfo);
            return userService.getByUsername(username);
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid username");
        }
    }

    public User verifyAuthentication(String username, String password) {
        try {
            User user = userService.getByUsername(username);
            if (!user.getPassword().equals(password)) {
                throw new AuthenticationFailedException("Wrong username/password!");
            }
            return user;
        } catch (EntityNotFoundException e) {
            throw new AuthenticationFailedException("Wrong username/password!");
        }
    }

    public User tryGetUser(HttpSession session) {
        String currentUser = (String) session.getAttribute("currentUser");
        if (currentUser == null) {
            throw new AuthenticationFailedException("No logged user");
        }
        return userService.getByUsername(currentUser);
    }

    private String getUsername(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new UnauthorizedOperationException("Invalid authentication.");
        }
        return userInfo.substring(0, firstSpace);
    }

    private String getPassword(String userInfo) {
        int firstSpace = userInfo.indexOf(" ");
        if (firstSpace == -1) {
            throw new UnauthorizedOperationException("Invalid authentication.");
        }
        return userInfo.substring(firstSpace + 1);
    }
}
