package com.carpooling.main.controller;

import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.service.interfaces.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/travels")
public class TravelRestController {

    private final TravelService travelService;

    @Autowired
    public TravelRestController(TravelService travelService) {
        this.travelService = travelService;
    }

    @GetMapping("/allTravels")
    public ResponseEntity<List<Travel>> getAllTravels() {
        List<Travel> travels = travelService.getAllTravels();
        return ResponseEntity.ok(travels);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Travel> getTravelById(@PathVariable Integer id) {
        Optional<Travel> travel = travelService.getTravelById(id);
        return travel.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    // Get travels by organizer
    @GetMapping("/organizer/{organizerId}")
    public ResponseEntity<List<Travel>> getTravelsByOrganizer(@PathVariable Long organizerId) {
        User organizer = new User();
        organizer.setUserId(organizerId); // Assuming User object requires only ID for query
        List<Travel> travels = travelService.getTravelsByOrganizer(organizer);
        return ResponseEntity.ok(travels);
    }

    // Create or update a travel
    @PostMapping
    public ResponseEntity<Travel> createOrUpdateTravel(@RequestBody Travel travel) {
        Travel savedTravel = travelService.createOrUpdateTravel(travel);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedTravel);
    }

    // Delete a travel by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTravel(@PathVariable Integer id) {
        Optional<Travel> travel = travelService.getTravelById(id);
        if (travel.isPresent()) {
            travelService.deleteTravel(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
}
