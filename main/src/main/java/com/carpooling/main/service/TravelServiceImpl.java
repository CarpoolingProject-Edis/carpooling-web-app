package com.carpooling.main.service;

import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.TravelRepository;
import com.carpooling.main.service.interfaces.TravelService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;

    public TravelServiceImpl(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    @Override
    public List<Travel> getAllTravels() {
        return travelRepository.findAll();
    }

    @Override
    public Optional<Travel> getTravelById(Integer id) {
        return travelRepository.findById(id);
    }

    @Override
    public List<Travel> getTravelsByOrganizer(User organizer) {
        return travelRepository.findByOrganizer(organizer);
    }

    @Override
    public Travel createOrUpdateTravel(Travel travel) {
        return travelRepository.save(travel);
    }

    @Override
    public void deleteTravel(Integer id) {
        travelRepository.deleteById(id);
    }
}
