package com.carpooling.main.repository;

import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.Photo;
import com.carpooling.main.repository.interfaces.PhotoUrlRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class PhotoUrlRepositoryImpl implements PhotoUrlRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public PhotoUrlRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public Photo getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            Photo photo = session.get(Photo.class, id);
            if (photo == null) {
                throw new EntityNotFoundException("Photo", id);
            }
            return photo;
        }
    }

    @Override
    public void create(Photo photo) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(photo);
            session.getTransaction().commit();
        }
    }
}
