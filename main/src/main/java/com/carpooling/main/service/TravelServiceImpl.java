package com.carpooling.main.service;


import com.carpooling.main.exceptions.EntityDuplicateException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.exceptions.NoFreeSpotsException;
import com.carpooling.main.exceptions.UnauthorizedOperationException;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;
import com.carpooling.main.model.enums.ApplicationStatus;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.repository.interfaces.TravelRepository;
import com.carpooling.main.service.interfaces.TravelRequestService;
import com.carpooling.main.service.interfaces.TravelService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelServiceImpl implements TravelService {

    private final TravelRepository travelRepository;
    private final TravelRequestService travelRequestService;

    @Autowired
    public TravelServiceImpl(TravelRepository travelRepository, TravelRequestService travelRequestService) {
        this.travelRepository = travelRepository;
        this.travelRequestService = travelRequestService;
    }
    @Transactional
    @Override
    public Travel getTravelById(int id) {
        return travelRepository.getTravelById(id);
    }
    @Override
    public List<Travel> getAllAvailableTravels() {
        return travelRepository.getAllTravels().stream()
                .filter(travel -> travel.getTravelStatus() == TravelStatus.OPEN)
                .toList();
    }



    @Override
    public List<Travel> getTravelsByStartingLocation(String location) {
        return travelRepository.getTravelsByStartingLocation(location);
    }

    @Override
    public List<Travel> getTravelsByDestination(String location) {
        return travelRepository.getTravelsByDestination(location);
    }

    @Override
    public List<Travel> getTravelsByUser(User user) {
        return travelRepository.getTravelsByUser(user);
    }

    @Override
    public List<Travel> getTravelsByDriver(User user) {
        return travelRepository.getTravelsByDriver(user);
    }


    @Override
    public void create(Travel travel) {
        boolean duplicateExits = true;

        try {
            travelRepository.getTravelById(travel.getId());
        } catch (EntityNotFoundException e) {
            duplicateExits = false;
        }

        if (duplicateExits) {
            throw new EntityDuplicateException("Travel", "id", travel.getId());
        }
        travelRepository.create(travel);
    }

    @Override
    public void update(Travel travel) {
        travelRepository.update(travel);
    }

    @Override
    public void delete(User loggedInUser, int id) {
        checkAdminOrCreator(loggedInUser, id);
        Travel travel = travelRepository.getTravelById(id);
        List<TravelRequest> travelRequests = travelRequestService.getAllRequestsForTravel(travel);
        for (TravelRequest travelRequest : travelRequests) {
            travelRequestService.delete(travelRequest);
        }
        travelRepository.delete(id);
    }

    @Override
    public void changeStatusToOpen(User loggedInUser, Travel travel) {
        checkAdminOrCreator(loggedInUser, travel);
        travelRepository.changeStatusToOpen(travel);
    }

    @Override
    public void changeStatusToFinished(User loggedInUser, Travel travel) {
        checkAdminOrCreator(loggedInUser, travel);
        travelRepository.changeStatusToFinished(travel);
    }

    @Override
    public void changeStatusToFinished(Travel travel) {
        travelRepository.changeStatusToFinished(travel);
    }

    @Override
    public void changeStatusToOngoing(Travel travel) {
        travelRepository.changeStatusToOngoing(travel);
    }

    @Override
    public void changeStatusToFull(User loggedInUser, Travel travel) {
        checkAdminOrCreator(loggedInUser, travel);
        travelRepository.changeStatusToFull(travel);
    }

    @Override
    public void changeStatusToCancelled(User loggedInUser, Travel travel) {
        checkAdminOrCreator(loggedInUser, travel);
        travelRepository.changeStatusToCancelled(travel);
    }

    private void checkAdminOrCreator(User loggedInUser, Travel travel) {
        if (!(loggedInUser.getUserRole().toString().equals("Admin")
                || loggedInUser.getId() == travel.getDriver().getId())) {
            throw new UnauthorizedOperationException("Only admins or active creators can modify!");
        }
    }

    private void checkAdminOrCreator(User loggedInUser, int accountId) {
        if (!(loggedInUser.getUserRole().toString().equals("Admin")
                || loggedInUser.getId() != accountId)) {
            throw new UnauthorizedOperationException("Only admins or active creators can modify!");
        }
    }

    @Override
    public void setApplicationToApproved(Travel travel, TravelRequest travelRequest) {
        int freeSpots = travel.getFreeSpots();
        if (freeSpots == 0 || travel.getTravelStatus().equals(TravelStatus.FULL)) {
            throw new NoFreeSpotsException("Your travel is full!");
        }
        travel.addPassenger(travelRequest.getPassenger());
        travelRequest.setApplicationStatus(ApplicationStatus.APPROVED);
        travel.setFreeSpots(freeSpots - 1);
        if (travel.getFreeSpots() == 0) {
            travel.setTravelStatus(TravelStatus.FULL);
        }
        update(travel);
    }

    @Override
    public void setApplicationToDeclined(Travel travel, TravelRequest travelRequest) {
        if (travelRequest.getApplicationStatus().equals(ApplicationStatus.APPROVED)) {
            travelRequest.setApplicationStatus(ApplicationStatus.DECLINED);
            int freeSpot = travel.getFreeSpots();
            travel.setFreeSpots(freeSpot + 1);
            if (travel.getTravelStatus().equals(TravelStatus.FULL)) {
                travel.setTravelStatus(TravelStatus.OPEN);
            }
            update(travel);
        }
        travelRequest.setApplicationStatus(ApplicationStatus.DECLINED);
        update(travel);
    }

    @Override
    public void setApprovedApplicationToDeclined(Travel travel, TravelRequest travelRequest) {
        int freeSpots = travel.getFreeSpots();
        travel.removePassenger(travelRequest.getPassenger());
        travel.setFreeSpots(freeSpots + 1);
        setApplicationToDeclined(travel, travelRequest);
    }
}

