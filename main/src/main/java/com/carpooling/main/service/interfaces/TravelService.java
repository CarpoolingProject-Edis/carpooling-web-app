package com.carpooling.main.service.interfaces;

import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;

import java.util.List;
import java.util.Optional;

public interface TravelService {

    List<Travel> getAllTravels();

    Optional<Travel> getTravelById(Integer id);

    List<Travel> getTravelsByOrganizer(User organizer);

    Travel createOrUpdateTravel(Travel travel);

    void deleteTravel(Integer id);
}
