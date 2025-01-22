package com.carpooling.main.repository.interfaces;


import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;

import java.util.List;

public interface TravelRequestRepository {

    List<TravelRequest> getAll();

    TravelRequest getById(int id);

    TravelRequest getByApplicantAndTravel(User user, Travel travel);

    void getByApplicant(User user);

    List<TravelRequest> getTravelRequestsByPassenger(User user);

    List<TravelRequest> getTravelRequestsByDriver(User user);

    List<TravelRequest> getAllByTravel(Travel travel);

    void create(TravelRequest travelRequest);

    void update(TravelRequest travelRequest);

    void delete(TravelRequest travelRequest);
}
