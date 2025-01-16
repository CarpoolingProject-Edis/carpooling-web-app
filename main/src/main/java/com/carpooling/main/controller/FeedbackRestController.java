package com.carpooling.main.controller;

import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.User;
import com.carpooling.main.service.interfaces.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/feedbacks")
public class FeedbackRestController {

    private final FeedbackService feedbackService;

    @Autowired
    public FeedbackRestController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }

    @GetMapping
    public ResponseEntity<List<Feedback>> getAllFeedbacks() {
        return ResponseEntity.ok(feedbackService.getAllFeedbacks());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Feedback> getFeedbackById(@PathVariable Integer id) {
        return feedbackService.getFeedbackById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/travel/{travelId}")
    public ResponseEntity<List<Feedback>> getFeedbacksByTravel(@PathVariable Travel travel) {
        return ResponseEntity.ok(feedbackService.getFeedbacksByTravel(travel));
    }

    @GetMapping("/from/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksFromUser(@PathVariable User fromUser) {
        return ResponseEntity.ok(feedbackService.getFeedbacksFromUser(fromUser));
    }

    @GetMapping("/to/{userId}")
    public ResponseEntity<List<Feedback>> getFeedbacksToUser(@PathVariable User toUser) {
        return ResponseEntity.ok(feedbackService.getFeedbacksToUser(toUser));
    }

    @PostMapping
    public ResponseEntity<Feedback> createFeedback(@RequestBody Feedback feedback) {
        return ResponseEntity.ok(feedbackService.createOrUpdateFeedback(feedback));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Feedback> updateFeedback(@PathVariable Integer id, @RequestBody Feedback feedback) {
        feedback.setFeedbackId(id);
        return ResponseEntity.ok(feedbackService.createOrUpdateFeedback(feedback));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFeedback(@PathVariable Integer id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.noContent().build();
    }
}
