package com.carpooling.main.repository;


import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.Travel;
import com.carpooling.main.model.enums.TravelStatus;
import com.carpooling.main.repository.interfaces.TravelRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.carpooling.main.model.User;
import java.util.List;

@Repository
public class TravelRepositoryImpl implements TravelRepository {
    private final SessionFactory sessionFactory;

    @Autowired
    public TravelRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Travel> getAllTravels() {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery("from Travel", Travel.class);
            return query.list();
        }
    }

    @Override
    public Travel getTravelById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Travel travel = session.get(Travel.class, id);
            if (travel == null) {
                throw new EntityNotFoundException("Travel", id);
            }
            return travel;
        }
    }

    @Override
    public List<Travel> getTravelsByStartingLocation(String location) {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery
                    ("from Travel where startPoint = :location", Travel.class);
            return query.list();
        }
    }

    @Override
    public List<Travel> getTravelsByDestination(String location) {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery
                    ("from Travel where endPoint = :location", Travel.class);
            return query.list();
        }
    }

    @Override
    public List<Travel> getTravelsByUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM Travel t WHERE t.driver = :user OR :user MEMBER OF t.passengers";
            Query<Travel> query = session.createQuery(hql, Travel.class);
            query.setParameter("user", user);
            return query.list();
        }
    }

    @Override
    public List<Travel> getTravelsByDriver(User user) {
        try (Session session = sessionFactory.openSession()) {
            Query<Travel> query = session.createQuery
                    ("from Travel where driver = :user", Travel.class);
            return query.list();
        }
    }

    @Override
    public void create(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.persist(travel);
        }
    }

    @Override
    public void updateArrivalTime(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        Travel travel = getTravelById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void changeStatusToOpen(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            travel.setTravelStatus(TravelStatus.OPEN);
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void changeStatusToFinished(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            travel.setTravelStatus(TravelStatus.FINISHED);
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void changeStatusToOngoing(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            travel.setTravelStatus(TravelStatus.ONGOING);
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void changeStatusToFull(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            travel.setTravelStatus(TravelStatus.FULL);
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void changeStatusToCancelled(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            travel.setTravelStatus(TravelStatus.CANCELLED);
            session.merge(travel);
            session.getTransaction().commit();
        }
    }

}
