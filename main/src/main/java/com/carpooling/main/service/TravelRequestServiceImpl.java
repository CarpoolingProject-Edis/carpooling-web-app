package com.carpooling.main.service;


import com.carpooling.main.exceptions.AlreadyAppliedException;
import com.carpooling.main.exceptions.TravelFinishedException;
import com.carpooling.main.exceptions.YouAreTheDriverException;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;
import com.carpooling.main.model.enums.ApplicationStatus;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.repository.interfaces.TravelRequestRepository;
import com.carpooling.main.service.interfaces.TravelRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelRequestServiceImpl implements TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;

    public TravelRequestServiceImpl(TravelRequestRepository travelRequestRepository) {
        this.travelRequestRepository = travelRequestRepository;
    }

    @Override
    public List<TravelRequest> getAll() {
        return travelRequestRepository.getAll();
    }

    @Override
    public TravelRequest getById(int id) {
        return travelRequestRepository.getById(id);
    }

    @Override
    public TravelRequest getByApplicantAndTravel(User user, Travel travel) {
        return travelRequestRepository.getByApplicantAndTravel(user, travel);
    }

    @Override
    public void getByApplicant(User user) {
        travelRequestRepository.getByApplicant(user);
    }

    @Override
    public List<TravelRequest> getTravelRequestsByPassenger(User user) {
        return travelRequestRepository.getTravelRequestsByPassenger(user);
    }

    @Override
    public List<TravelRequest> getTravelRequestsByDriver(User user) {
        return travelRequestRepository.getTravelRequestsByDriver(user);
    }

    @Override
    public List<TravelRequest> getTravelRequestsByPopulate(User receiver, int travelId) {
        return getTravelRequestsByPassenger(receiver)
                .stream()
                .filter(request -> request.getTravel().getId() == travelId)
                .collect(Collectors.toList());
    }

    @Override
    public List<TravelRequest> getAllRequestsForTravel(Travel travel) {
        return travelRequestRepository.getAllByTravel(travel);
    }

    @Transactional
    @Override
    public void create(TravelRequest travelRequest, User loggedInUser, Travel travel) {
        if (travel.getTravelStatus().equals(TravelStatus.FINISHED)) {
            throw new TravelFinishedException("This travel has already been concluded.");
        }
        if (travel.getDriver().getId() == loggedInUser.getId()) {
            throw new YouAreTheDriverException("Are you dumb?");
        }
        if (travel.getPassengers().contains(loggedInUser)) {
            throw new AlreadyAppliedException("You have already applied for this travel.");
        }
        travelRequest.setPassenger(loggedInUser);
        travelRequest.setTravel(travel);
        travelRequest.setApplicationStatus(ApplicationStatus.PENDING);
        travelRequestRepository.create(travelRequest);
    }


    @Override
    public void update(TravelRequest travelRequest) {
        travelRequestRepository.update(travelRequest);
    }

    @Override
    public void delete(TravelRequest travelRequest) {
        travelRequestRepository.delete(travelRequest);
    }
}
