package com.carpooling.main.service.interfaces;


import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;

import java.util.List;

public interface TravelRequestService {

    List<TravelRequest> getAll();

    TravelRequest getById(int id);

    TravelRequest getByApplicantAndTravel(User user, Travel travel);

    void getByApplicant(User user);

    List<TravelRequest> getTravelRequestsByPassenger(User user);

    List<TravelRequest> getTravelRequestsByDriver(User user);

    List<TravelRequest> getTravelRequestsByPopulate(User receiver, int travelId);

    List<TravelRequest> getAllRequestsForTravel(Travel travel);

    void create(TravelRequest travelRequest, User loggedInUser, Travel travel);

    void update(TravelRequest travelRequest);

    void delete(TravelRequest travelRequest);
}
