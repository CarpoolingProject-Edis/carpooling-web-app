package com.carpooling.main.controller.MVC;

import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.helpers.AuthenticationHelper;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.model.dto.CreateTravelDto;
import com.carpooling.main.service.interfaces.TravelService;
import com.carpooling.main.helpers.mapper.TravelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/travels")
public class TravelMVCController {
    private final TravelService travelService;
    private final AuthenticationHelper authenticationHelper;
    private final TravelMapper travelMapper;

    @Autowired
    public TravelMVCController(TravelService travelService, AuthenticationHelper authenticationHelper, TravelMapper travelMapper) {
        this.travelService = travelService;
        this.authenticationHelper = authenticationHelper;
        this.travelMapper = travelMapper;
    }

    @GetMapping("/{id}")
    public String getTravelById(@PathVariable int id, Model model) {
        try {
            Travel travel = travelService.getTravelById(id);
            model.addAttribute("travel", travel);
            return "SingleTravelView";  // Returns a Thymeleaf or JSP view (travel-details.html/jsp)
        } catch (EntityNotFoundException e) {
            model.addAttribute("error", e.getMessage());
            return "error";  // Redirects to an error page
        }
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("travelDTO", new CreateTravelDto());
        return "travel-form";  // Renders travel creation form
    }

    @PostMapping("/create")
    public String createTravel(@RequestHeader HttpHeaders headers,
                               @Valid @ModelAttribute("travelDTO") CreateTravelDto travelDTO,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "travel-form";  // Return form with validation errors
        }

        try {
            User user = authenticationHelper.tryGetUser(headers);
            Travel travel = travelMapper.fromDTO(travelDTO, user);
            travelService.create(travel);
            return "redirect:/travels/" + travel.getId();  // Redirect to travel details
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "error";  // Redirect to an error page
        }
    }
}
