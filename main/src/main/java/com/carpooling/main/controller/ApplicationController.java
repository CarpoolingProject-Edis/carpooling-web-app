package com.carpooling.main.controller;

import com.carpooling.main.dto.ApplicationDTO;
import com.carpooling.main.enums.ApplicationStatus;
import com.carpooling.main.model.Application;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.service.interfaces.ApplicationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService applicationService;

    public ApplicationController(ApplicationService applicationService) {
        this.applicationService = applicationService;
    }

    @GetMapping
    public ResponseEntity<List<ApplicationDTO>> getAllApplications() {
        List<Application> applications = applicationService.getAllApplications();
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDTO> getApplicationById(@PathVariable Integer id) {
        Optional<Application> application = applicationService.getApplicationById(id);
        return application.map(value -> new ResponseEntity<>(convertToDTO(value), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/travel/{travelId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByTravel(@PathVariable Integer travelId) {
        Travel travel = new Travel(); // Replace with the logic to retrieve Travel entity by ID
        travel.setTravelId(travelId);
        List<Application> applications = applicationService.getApplicationsByTravel(travel);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByUser(@PathVariable Long userId) {
        User user = new User(); // Replace with the logic to retrieve User entity by ID
        user.setUserId(userId);
        List<Application> applications = applicationService.getApplicationsByUser(user);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);
    }

    @GetMapping("/travel/{travelId}/status/{status}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByTravelAndStatus(
            @PathVariable Integer travelId,
            @PathVariable ApplicationStatus status) {
        Travel travel = new Travel(); // Replace with the logic to retrieve Travel entity by ID
        travel.setTravelId(travelId);
        List<Application> applications = applicationService.getApplicationsByTravelAndStatus(travel, status);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);
    }

    @GetMapping("/user/{userId}/status/{status}")
    public ResponseEntity<List<ApplicationDTO>> getApplicationsByUserAndStatus(
            @PathVariable Long userId,
            @PathVariable ApplicationStatus status) {
        User user = new User(); // Replace with the logic to retrieve User entity by ID
        user.setUserId(userId);
        List<Application> applications = applicationService.getApplicationsByUserAndStatus(user, status);
        List<ApplicationDTO> applicationDTOs = applications.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new ResponseEntity<>(applicationDTOs, HttpStatus.OK);
    }

    @PostMapping("/api/applications")
    public ResponseEntity<ApplicationDTO> createOrUpdateApplication(@RequestBody Application application) {
        Application savedApplication = applicationService.createOrUpdateApplication(application);
        return new ResponseEntity<>(convertToDTO(savedApplication), HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable Integer id) {
        applicationService.deleteApplication(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private ApplicationDTO convertToDTO(Application application) {
        return new ApplicationDTO(
                application.getApplicationId(),
                application.getTravel().getTravelId(),
                application.getTravel().getStartPoint(),
                application.getTravel().getEndPoint(),
                application.getUser().getUserId(),
                application.getUser().getUsername(),
                application.getStatus(),
                application.getAppliedAt()
        );
    }

}
