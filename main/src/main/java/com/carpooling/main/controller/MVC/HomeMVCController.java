package com.carpooling.main.controller.MVC;

import com.carpooling.main.exceptions.AuthenticationFailedException;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.model.User;
import com.carpooling.main.model.enums.UserRole;
import com.carpooling.main.model.enums.UserStatus;
import com.carpooling.main.service.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeMVCController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;

    @Autowired
    public HomeMVCController(UserService userService, AuthenticationHelper authenticationHelper) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
    }

    @ModelAttribute("isAuthenticated")
    public boolean populateIsAuthenticated(HttpSession session) {
        return session.getAttribute("currentUser") != null;
    }

    @ModelAttribute("isAdmin")
    public boolean populateIsAdmin(HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            return currentUser != null && currentUser.getUserRole() == UserRole.ADMIN;
        } catch (AuthenticationFailedException e) {
            return false;
        }
    }

    @ModelAttribute("isBlock")
    public boolean populateIsBlock(HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            return currentUser != null && currentUser.getUserStatus() == UserStatus.BLOCKED;
        } catch (AuthenticationFailedException e) {
            return false;
        }
    }
    @GetMapping
    public String HomePage(Model model, HttpSession session) {
        List<User> top10UsersDriver = userService.getUsersWithHighestDriverRating(10);
        model.addAttribute("top10UsersDriver", top10UsersDriver != null ? top10UsersDriver : List.of());

        List<User> top10UsersPassenger = userService.getUsersWithHighestPassengerRating(10);
        model.addAttribute("top10UsersPassenger", top10UsersPassenger != null ? top10UsersPassenger : List.of());

        try {
            User logUser = authenticationHelper.tryGetUser(session);
            if (logUser != null) {
                model.addAttribute("logUser", logUser);
                boolean hasNoVehicle = logUser.getCar() == null || logUser.getCar().getId() == 1;
                model.addAttribute("hasNoVehicle", hasNoVehicle);
            } else {
                model.addAttribute("logUser", null);
                model.addAttribute("hasNoVehicle", true);
            }
        } catch (AuthenticationFailedException e) {
            model.addAttribute("logUser", null);
            model.addAttribute("hasNoVehicle", true);
        }

        return "HomeView";
    }


    @GetMapping("/about")
    public String showAboutInfo() {
        return "AboutView";
    }
}
