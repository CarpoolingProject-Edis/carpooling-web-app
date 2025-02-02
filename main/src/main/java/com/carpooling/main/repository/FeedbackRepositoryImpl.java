package com.carpooling.main.repository;

import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.interfaces.FeedbackRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackRepositoryImpl implements FeedbackRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public FeedbackRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Feedback getFeedbackById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Feedback feedback = session.get(Feedback.class, id);
            if (feedback == null) {
                throw new EntityNotFoundException("Feedback", id);
            }
            return feedback;
        }
    }

    public Feedback getFeedbackByGiverAndReceiver(User loggedInUser, User receiver) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery(
                    "from Feedback where createdBy = :giver and receivedBy = :receiver", Feedback.class);
            query.setParameter("giver", loggedInUser).setParameter("receiver", receiver);
            List<Feedback> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException(String.format("No feedback from %s to %s.",
                        loggedInUser.getUsername(),
                        receiver.getUsername()));
            }
            return result.get(0);
        }
    }

    @Override
    public boolean feedbackByGiverAndReceiverExists(User giver, User receiver) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery(
                    "from Feedback where createdBy = :giver and receivedBy = :receiver", Feedback.class);
            query.setParameter("giver", giver).setParameter("receiver", receiver);
            List<Feedback> result = query.list();
            return !result.isEmpty();
        }
    }

    @Override
    public List<Feedback> getFeedbacks() {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery("from Feedback", Feedback.class);
            return query.list();
        }
    }

    @Override
    public List<Feedback> getFeedbacksByReceiver(User receiver) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery("from Feedback where receivedBy = :receiver", Feedback.class);
            query.setParameter("receiver", receiver);
            List<Feedback> result = query.list();
            if (result.isEmpty()) {
                return null;
            }
            return query.list();
        }
    }

    @Override
    public List<Feedback> getUncommentedFeedbacksByReceiver(User receiver) {
        try (Session session = sessionFactory.openSession()) {
            Query<Feedback> query = session.createQuery(
                    "select f from Feedback f " +
                            "where f.receivedBy = :receiver",
                    Feedback.class);
            query.setParameter("receiver", receiver);
            return query.list();
        }
    }


    @Override
    public void create(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            session.save(feedback);
        }
    }

    @Override
    public void update(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(feedback);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(feedback);
            session.getTransaction().commit();
        }
}
}
