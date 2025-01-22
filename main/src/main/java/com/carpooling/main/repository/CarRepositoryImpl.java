package com.carpooling.main.repository;


import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.Car;
import com.carpooling.main.repository.interfaces.CarRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Repository
public class CarRepositoryImpl implements CarRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public CarRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Car> get() {
        try (Session session = sessionFactory.openSession()) {
            Query<Car> query = session.createQuery("from Car", Car.class);
            return query.list();
        }
    }

    @Override
    public Car getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Car car = session.get(Car.class, id);
            if (car == null) {
                throw new EntityNotFoundException("Car", id);
            }
            return car;
        }
    }

    @Override
    public void create(Car car) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(car);
            session.getTransaction().commit();
        }
    }
}
