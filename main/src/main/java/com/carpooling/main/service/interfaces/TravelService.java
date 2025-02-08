package com.carpooling.main.service.interfaces;

import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;

import java.util.List;

public interface TravelService {

    Travel getTravelById(int id);

    List<Travel> getAllAvailableTravels();

    List<Travel> getTravelsByStartingLocation(String location);

    List<Travel> getTravelsByDestination(String location);

    List<Travel> getTravelsByUser(User user);

    List<Travel> getTravelsByDriver(User user);

    List<Travel> getOngoingUserTravels(User user, Travel travel);

    void create(Travel travel);

    void update(Travel travel);

    void delete(User loggedInUser, int id);

    void changeStatusToOpen(User loggedInUser, Travel travel);

    void changeStatusToFinished(User loggedInUser, Travel travel);

    void changeStatusToFinished(Travel travel);

    void changeStatusToOngoing(Travel travel);

    void changeStatusToFull(User loggedInUser, Travel travel);

    void changeStatusToCancelled(User loggedInUser, Travel travel);

    void setApplicationToApproved(Travel travel, TravelRequest travelRequest);

    void setApplicationToDeclined(Travel travel, TravelRequest travelRequest);

    void setApprovedApplicationToDeclined(Travel travel, TravelRequest travelRequest);
}
