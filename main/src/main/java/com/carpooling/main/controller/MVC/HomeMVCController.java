package com.carpooling.main.controller.MVC;

import com.carpooling.main.exceptions.AuthenticationFailedException;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.interfaces.TravelRepository;
import com.carpooling.main.service.interfaces.TravelService;
import com.carpooling.main.service.interfaces.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/home")
public class HomeMVCController {

    private final UserService userService;
    private final AuthenticationHelper authenticationHelper;
    private final TravelService travelService;
    private final TravelRepository travelRepository;

    @Autowired
    public HomeMVCController(UserService userService, AuthenticationHelper authenticationHelper, TravelService travelService, TravelRepository travelRepository) {
        this.authenticationHelper = authenticationHelper;
        this.userService = userService;
        this.travelService = travelService;
        this.travelRepository = travelRepository;
    }

    @GetMapping
    public String HomePage(@RequestParam(value = "filter", required = false) String filter, Model model, HttpSession session) {

        List<Travel> availableTravels = travelRepository.getAllTravels();

        if (filter != null) {
            availableTravels = switch (filter) {
                case "start" -> availableTravels.stream()
                        .sorted(Comparator.comparing(Travel::getStartPoint))
                        .collect(Collectors.toList());
                case "end" -> availableTravels.stream()
                        .sorted(Comparator.comparing(Travel::getEndPoint))
                        .collect(Collectors.toList());
                case "time" -> availableTravels.stream()
                        .sorted(Comparator.comparing(Travel::getDepartureTime))
                        .collect(Collectors.toList());
                case "a-z" -> availableTravels.stream()
                        .sorted(Comparator.comparing(Travel::getStartPoint)
                                .thenComparing(Travel::getEndPoint)
                                .thenComparing(Travel::getDepartureTime))
                        .collect(Collectors.toList());
                case "z-a" -> availableTravels.stream()
                        .sorted(Comparator.comparing(Travel::getStartPoint)
                                .thenComparing(Travel::getEndPoint)
                                .thenComparing(Travel::getDepartureTime)
                                .reversed())
                        .collect(Collectors.toList());
                default -> availableTravels;
            };
        }

        model.addAttribute("availableTravels", availableTravels);

        List<User> top10UsersDriver = userService.getUsersWithHighestDriverRating(10);
        model.addAttribute("top10UsersDriver", top10UsersDriver != null ? top10UsersDriver : List.of());

        List<User> top10UsersPassenger = userService.getUsersWithHighestPassengerRating(10);
        model.addAttribute("top10UsersPassenger", top10UsersPassenger != null ? top10UsersPassenger : List.of());

        try {
            User logUser = authenticationHelper.tryGetUser(session);
            model.addAttribute("logUser", logUser);
            model.addAttribute("hasNoVehicle", logUser == null || logUser.getCar() == null || logUser.getCar().getId() == 1);
        } catch (AuthenticationFailedException e) {
            model.addAttribute("logUser", null);
            model.addAttribute("hasNoVehicle", true);
        }

        return "HomeView";
    }
}
