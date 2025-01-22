package com.carpooling.main.model;

import com.carpooling.main.model.Feedback;
import jakarta.persistence.*;

@Entity
@Table(name = "feedback_comments")
public class FeedbackComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @OneToOne
    @JoinColumn(name = "feedback_id")
    private Feedback feedback;

    @Column(name = "content")
    private String comment;

    public FeedbackComment() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String content) {
        this.comment = content;
    }

    public Feedback getFeedback() {
        return feedback;
    }

    public void setFeedback(Feedback feedback) {
        this.feedback = feedback;
    }
}
