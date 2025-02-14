package com.carpooling.main.controller.MVC;

import com.carpooling.main.exceptions.AuthenticationFailedException;
import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.helpers.mapper.UserMapper;
import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.LoginDto;
import com.carpooling.main.model.dto.RegisterDto;
import com.carpooling.main.model.enums.UserStatus;
import com.carpooling.main.service.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/auth")
public class AuthenticationMVCController {
    private final AuthenticationHelper authenticationHelper;
    private final UserMapper userMapper;
    private final UserService userService;
    //private final PasswordValidator passwordValidator;

    public AuthenticationMVCController(AuthenticationHelper authenticationHelper, UserMapper userMapper, UserService userService) {
        this.authenticationHelper = authenticationHelper;
        this.userMapper = userMapper;
        this.userService = userService;

    }


    @ModelAttribute("isBlock")
    public boolean populateIsBlock(HttpSession session) {
        try {
            User currentUser = authenticationHelper.tryGetUser(session);
            return currentUser.getUserStatus().equals(UserStatus.BLOCKED);
        } catch (AuthenticationFailedException e) {
            return false;
        }
    }

    @GetMapping("/login")
    public String showLoginPage(Model model) {
        model.addAttribute("login", new LoginDto());
        return "auth/LoginView";
    }

    @PostMapping("/login")
    public String handleLogin(@Valid @ModelAttribute("login") LoginDto dto,
                              BindingResult bindingResult,
                              HttpSession session) {
        if (bindingResult.hasErrors()) {
            return "auth/LoginView";
        }

        try {
            authenticationHelper.verifyAuthentication(dto.getUsername(), dto.getPassword());
            session.setAttribute("currentUser", dto.getUsername());
            return "redirect:/home";
        } catch (AuthenticationFailedException e) {
            bindingResult.rejectValue("username", "Auth_error", e.getMessage());
            return "auth/LoginView";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.removeAttribute("currentUser");
        return "redirect:/home";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("register", new RegisterDto());
        return "auth/RegisterView";
    }

    @PostMapping("/register")
    public String handleRegister(@Valid @ModelAttribute("register") RegisterDto register,
                                 BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "auth/RegisterView";
        }

        if (!register.getPassword().equals(register.getConfirmPassword())) {
            bindingResult.rejectValue("confirmPassword", "error", "The passwords don`t match!");
            return "auth/RegisterView";
        }

        try {
            User user = userMapper.fromDto(register);
            userService.create(user);
            return "redirect:/auth/login";
        } catch (EntityDuplicateException e) {
            bindingResult.rejectValue("username", "username-error", e.getMessage());
            return "auth/RegisterView";
        }
    }
}
