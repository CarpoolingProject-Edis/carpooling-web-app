package com.carpooling.main.repository;

import com.carpooling.main.enums.ApplicationStatus;
import com.carpooling.main.model.Application;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, Integer> {

    List<Application> findByTravel(Travel travel);

    List<Application> findByUser(User user);

    List<Application> findByTravelAndStatus(Travel travel, ApplicationStatus status);

    List<Application> findByUserAndStatus(User user, ApplicationStatus status);
}
