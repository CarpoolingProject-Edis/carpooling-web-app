package com.carpooling.main.repository;

import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.Feedback;
import com.carpooling.main.model.FeedbackComment;
import com.carpooling.main.model.User;
import com.carpooling.main.repository.interfaces.FeedbackCommentRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class FeedbackCommentRepositoryImpl implements FeedbackCommentRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public FeedbackCommentRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<FeedbackComment> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<FeedbackComment> query =
                    session.createQuery("from FeedbackComment", FeedbackComment.class);
            return query.list();
        }
    }

    @Override
    public List<FeedbackComment> getCommentsByReceiver(User receiver) {
        try (Session session = sessionFactory.openSession()) {
            Query<FeedbackComment> query = session.createQuery(
                    "select fc from FeedbackComment fc inner join fc.feedback f where f.receivedBy = :receiver", FeedbackComment.class);
            query.setParameter("receiver", receiver);
            List<FeedbackComment> result = query.list();
            return result.isEmpty() ? List.of() : result;
        }
    }

    @Override
    public FeedbackComment getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            FeedbackComment feedbackComment = session.get(FeedbackComment.class, id);
            if (feedbackComment == null) {
                throw new EntityNotFoundException("Feedback comment", id);
            }
            return feedbackComment;
        }
    }

    @Override
    public FeedbackComment getCommentByFeedback(Feedback feedback) {
        try (Session session = sessionFactory.openSession()) {
            Query<FeedbackComment> query =
                    session.createQuery("from FeedbackComment where feedback = :feedback", FeedbackComment.class);
            query.setParameter("feedback", feedback);
            List<FeedbackComment> result = query.list();
            return result.isEmpty() ? null : result.get(0);
        }
    }

    @Override
    public void create(FeedbackComment feedbackComment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.persist(feedbackComment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(FeedbackComment feedbackComment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(feedbackComment);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(FeedbackComment feedbackComment) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(feedbackComment);
            session.getTransaction().commit();
        }
    }
}
