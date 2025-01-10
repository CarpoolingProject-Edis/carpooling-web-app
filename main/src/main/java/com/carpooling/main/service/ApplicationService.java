package com.carpooling.main.service;

import com.carpooling.main.enums.ApplicationStatus;
import com.carpooling.main.model.Application;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.ApplicationRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationService(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    public Optional<Application> getApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    public List<Application> getApplicationsByTravel(Travel travel) {
        return applicationRepository.findByTravel(travel);
    }

    public List<Application> getApplicationsByUser(User user) {
        return applicationRepository.findByUser(user);
    }

    public List<Application> getApplicationsByTravelAndStatus(Travel travel, ApplicationStatus status) {
        return applicationRepository.findByTravelAndStatus(travel, status);
    }

    public List<Application> getApplicationsByUserAndStatus(User user, ApplicationStatus status) {
        return applicationRepository.findByUserAndStatus(user, status);
    }

    public Application createOrUpdateApplication(Application application) {
        return applicationRepository.save(application);
    }

    public void deleteApplication(Integer id) {
        applicationRepository.deleteById(id);
    }
}
