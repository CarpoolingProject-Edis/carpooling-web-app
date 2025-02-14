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
            session.save(travel);
        }
    }

    @Override
    public void updateArrivalTime(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(Travel travel) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.update(travel);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Travel travel = session.get(Travel.class, id);
            if (travel == null) {
                System.out.println("Travel with ID " + id + " not found!");
                throw new EntityNotFoundException("Travel not found with ID: " + id);
            }

            session.delete(travel);
            session.flush(); // Ensures changes are persisted immediately
            session.getTransaction().commit();
            System.out.println("Deleted travel with ID: " + id);
        } catch (Exception e) {
            e.printStackTrace();
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

            Travel existingTravel = session.get(Travel.class, travel.getId());
            if (existingTravel == null) {
                throw new EntityNotFoundException("Travel not found.");
            }

            existingTravel.setTravelStatus(TravelStatus.FINISHED);
            session.update(existingTravel);  // Ensure update instead of merge
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
