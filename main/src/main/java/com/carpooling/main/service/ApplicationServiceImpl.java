package com.carpooling.main.service;

import com.carpooling.main.enums.ApplicationStatus;
import com.carpooling.main.model.Application;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.ApplicationRepository;
import com.carpooling.main.service.interfaces.ApplicationService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ApplicationServiceImpl implements ApplicationService {

    private final ApplicationRepository applicationRepository;

    public ApplicationServiceImpl(ApplicationRepository applicationRepository) {
        this.applicationRepository = applicationRepository;
    }

    @Override
    public List<Application> getAllApplications() {
        return applicationRepository.findAll();
    }

    @Override
    public Optional<Application> getApplicationById(Integer id) {
        return applicationRepository.findById(id);
    }

    @Override
    public List<Application> getApplicationsByTravel(Travel travel) {
        return applicationRepository.findByTravel(travel);
    }

    @Override
    public List<Application> getApplicationsByUser(User user) {
        return applicationRepository.findByUser(user);
    }

    @Override
    public List<Application> getApplicationsByTravelAndStatus(Travel travel, ApplicationStatus status) {
        return applicationRepository.findByTravelAndStatus(travel, status);
    }

    @Override
    public List<Application> getApplicationsByUserAndStatus(User user, ApplicationStatus status) {
        return applicationRepository.findByUserAndStatus(user, status);
    }

    @Override
    public Application createOrUpdateApplication(Application application) {
        return applicationRepository.save(application);
    }

    @Override
    public void deleteApplication(Integer id) {
        applicationRepository.deleteById(id);
    }
}
