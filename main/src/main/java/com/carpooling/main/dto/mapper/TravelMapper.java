package com.carpooling.main.dto.mapper;

import com.carpooling.main.dto.TravelDTO;
import com.carpooling.main.model.Travel;

public class TravelMapper {

    public static TravelDTO convertToDTO(Travel travel) {

        return new TravelDTO(
                travel.getTravelId(),
                travel.getOrganizer().getUserId(),
                travel.getOrganizer().getFullName(),
                travel.getStartPoint(),
                travel.getEndPoint(),
                travel.getDepartureTime(),
                travel.getFreeSpots(),
                travel.getComments()
        );
    }
}
