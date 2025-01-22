package com.carpooling.main.repository;


import com.carpooling.main.exceptions.AlreadyAppliedException;
import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.TravelRequest;
import com.carpooling.main.model.User;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.repository.interfaces.TravelRequestRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class TravelRequestRepositoryImpl implements TravelRequestRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public TravelRequestRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<TravelRequest> getAll() {
        try (Session session = sessionFactory.openSession()) {
            Query<TravelRequest> query = session.createQuery("from TravelRequest ", TravelRequest.class);
            return query.list();
        }
    }

    @Override
    public TravelRequest getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            TravelRequest travelRequest = session.get(TravelRequest.class, id);
            if (travelRequest == null) {
                throw new EntityNotFoundException("Travel request", id);
            }
            return travelRequest;
        }
    }

    @Override
    public TravelRequest getByApplicantAndTravel(User user, Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            Query<TravelRequest> query = session.createQuery
                    ("from TravelRequest where passenger = :user and travel = :travel", TravelRequest.class);
            query = query.setParameter("user", user).setParameter("travel", travel);
            List<TravelRequest> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("You have not applied for this travel.");
            }
            return result.get(0);
        }
    }

    @Override
    public void getByApplicant(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<TravelRequest> query = session.createQuery
                    ("from TravelRequest where passenger = :user", TravelRequest.class);
            query = query.setParameter("user", user);
            List<TravelRequest> result = query.list();
            if (!result.isEmpty()) {
                throw new AlreadyAppliedException("You have already applied for a travel.");
            }
        }
    }

    @Override
    public List<TravelRequest> getTravelRequestsByPassenger(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<TravelRequest> query = session.createQuery(
                    "from TravelRequest where passenger = :user", TravelRequest.class);
            query.setParameter("user", user);
            return query.list();
        }
    }

    @Override
    public List<TravelRequest> getTravelRequestsByDriver(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<TravelRequest> query = session.createQuery(
                    "from TravelRequest tr where tr.travel.driver = :user and " +
                            "(tr.travel.travelStatus = :open or tr.travel.travelStatus = :full)", TravelRequest.class);
            query.setParameter("user", user)
                    .setParameter("open", TravelStatus.OPEN)
                    .setParameter("full", TravelStatus.FULL);
            return query.list();
        }
    }

    @Override
    public List<TravelRequest> getAllByTravel(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            Query<TravelRequest> query = session.createQuery
                    ("from TravelRequest where travel = :travel", TravelRequest.class);
            query.setParameter("travel", travel);
            return query.list();
        }
    }

    @Override
    public void create(TravelRequest travelRequest) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(travelRequest);
        }
    }

    @Override
    public void update(TravelRequest travelRequest) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travelRequest);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(TravelRequest travelRequest) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(travelRequest);
            session.getTransaction().commit();
        }
    }
}
