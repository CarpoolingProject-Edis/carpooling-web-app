package com.carpooling.main.service;

import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.TravelRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelService {

    private final TravelRepository travelRepository;

    public TravelService(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    public Optional<Travel> getTravelById(Integer id) {
        return travelRepository.findById(id);
    }

    public List<Travel> getTravelsByOrganizer(User organizer) {
        return travelRepository.findByOrganizer(organizer);
    }

    public Travel createOrUpdateTravel(Travel travel) {
        return travelRepository.save(travel);
    }

    public void deleteTravel(Integer id) {
        travelRepository.deleteById(id);
    }
}
