package com.carpooling.main.service;


import com.carpooling.main.exceptions.*;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;
import com.carpooling.main.model.enums.ApplicationStatus;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.repository.interfaces.TravelRepository;
import com.carpooling.main.repository.interfaces.TravelRequestRepository;
import com.carpooling.main.service.interfaces.TravelRequestService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TravelRequestServiceImpl implements TravelRequestService {

    private final TravelRequestRepository travelRequestRepository;
    private final TravelRepository travelRepository;
    public TravelRequestServiceImpl(TravelRequestRepository travelRequestRepository, TravelRepository travelRepository) {
        this.travelRequestRepository = travelRequestRepository;
        this.travelRepository = travelRepository;
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

        Travel travel = travelRepository.getTravelById(travelId);

        if (travel.getDriver().getId() == receiver.getId()) {

            return travelRequestRepository.getAllByTravel(travel);
        } else {
            return getTravelRequestsByPassenger(receiver)
                    .stream()
                    .filter(request -> request.getTravel().getId() == travelId)
                    .collect(Collectors.toList());
        }
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

    @Override
    public void acceptRequest(User driver, int travelId, int requestId) {
        TravelRequest request = travelRequestRepository.getById(requestId);

        if (request == null || request.getTravel().getId() != travelId) {
            throw new EntityNotFoundException("Request not found.");
        }

        if (!request.getTravel().getDriver().equals(driver)) {
            throw new AuthenticationFailedException("Only the driver can accept requests.");
        }

        request.setApplicationStatus(ApplicationStatus.APPROVED);
        request.getTravel().getPassengers().add(request.getPassenger());

        travelRequestRepository.update(request);
        travelRepository.update(request.getTravel());
    }

    @Override
    public void rejectRequest(User driver, int travelId, int requestId) {
        TravelRequest request = travelRequestRepository.getById(requestId);

        if (request == null || request.getTravel().getId() != travelId) {
            throw new EntityNotFoundException("Request not found.");
        }

        if (!request.getTravel().getDriver().equals(driver)) {
            throw new AuthenticationFailedException("Only the driver can reject requests.");
        }

        request.setApplicationStatus(ApplicationStatus.DECLINED);
        travelRequestRepository.update(request);
    }




}
