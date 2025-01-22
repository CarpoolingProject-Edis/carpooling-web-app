package com.carpooling.main.repository.interfaces;


import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;

import java.util.List;

public interface TravelRepository {

    List<Travel> getAllTravels();

    Travel getTravelById(int id);

    List<Travel> getTravelsByStartingLocation(String location);

    List<Travel> getTravelsByDestination(String location);

    List<Travel> getTravelsByUser(User user);

    List<Travel> getTravelsByDriver(User user);

    void create(Travel travel);

    void update(Travel travel);

    void updateArrivalTime(Travel travel);

    void delete(int id);

    void changeStatusToOpen(Travel travel);

    void changeStatusToFinished(Travel travel);

    void changeStatusToOngoing(Travel travel);

    void changeStatusToFull(Travel travel);

    void changeStatusToCancelled(Travel travel);

}
