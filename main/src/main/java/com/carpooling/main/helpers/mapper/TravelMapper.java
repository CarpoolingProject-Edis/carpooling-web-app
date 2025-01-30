package com.carpooling.main.helpers.mapper;


import com.carpooling.main.model.Travel;
import com.carpooling.main.model.dto.CreateTravelDto;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.repository.interfaces.TravelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.carpooling.main.model.User;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
@Component
public class TravelMapper {
    private TravelRepository travelRepository;

    @Autowired
    public TravelMapper(TravelRepository travelRepository) {
        this.travelRepository = travelRepository;
    }

    public Travel fromDTO(CreateTravelDto travelDTO, User user) {
        Travel travel = new Travel();
        travel.setDriver(user);
        travel.setStartPoint(travelDTO.getStartPoint());
        travel.setEndPoint(travelDTO.getEndPoint());
        travel.setFreeSpots(travelDTO.getFree_spots());
        LocalDateTime departureTime = parseDepartureTime(travelDTO.getDeparture_time());
        travel.setDepartureTime(departureTime);
        travel.setTravelStatus(TravelStatus.OPEN);
        return travel;
    }

    private LocalDateTime parseDepartureTime(String departureTimeString) {
        if (departureTimeString == null || departureTimeString.isEmpty()) {
            throw new IllegalArgumentException("Departure time cannot be null or empty.");
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        return LocalDateTime.parse(departureTimeString, formatter);
    }


    private LocalDateTime calculatingArrivalTime(LocalDateTime departureTime, String duration) {
        String[] durationArr = duration.split(" ");
        int durationDigits = Integer.parseInt(durationArr[0]);
        return departureTime.plusMinutes(durationDigits);
    }
}
