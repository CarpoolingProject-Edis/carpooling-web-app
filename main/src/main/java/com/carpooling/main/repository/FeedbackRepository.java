package com.carpooling.main.repository;

import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FeedbackRepository extends JpaRepository<Feedback, Integer> {

    List<Feedback> findByTravel(Travel travel);

    List<Feedback> findByFromUser(User fromUser);

    List<Feedback> findByToUser(User toUser);
}
