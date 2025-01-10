package com.carpooling.main.service.interfaces;

import com.carpooling.main.enums.ApplicationStatus;
import com.carpooling.main.model.Application;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;

import java.util.List;
import java.util.Optional;

public interface ApplicationService {
    List<Application> getAllApplications();
    Optional<Application> getApplicationById(Integer id);
    List<Application> getApplicationsByTravel(Travel travel);
    List<Application> getApplicationsByUser(User user);
    List<Application> getApplicationsByTravelAndStatus(Travel travel, ApplicationStatus status);
    List<Application> getApplicationsByUserAndStatus(User user, ApplicationStatus status);
    Application createOrUpdateApplication(Application application);
    void deleteApplication(Integer id);
}
