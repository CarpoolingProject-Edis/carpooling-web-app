package com.carpooling.main.repository;


import com.carpooling.main.exceptions.EntityNotFoundException;
import com.carpooling.main.model.User;
import com.carpooling.main.model.enums.UserRole;
import com.carpooling.main.model.enums.UserStatus;
import com.carpooling.main.repository.interfaces.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final SessionFactory sessionFactory;

    @Autowired
    public UserRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public User getById(int id) {
        try (Session session = sessionFactory.openSession()) {
            User user = session.get(User.class, id);
            if (user == null) {
                throw new EntityNotFoundException("User", id);
            }
            return user;
        }
    }

    @Override
    public User getByUsername(String username) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where username = :username", User.class);
            query = query.setParameter("username", username);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "username", username);
            }
            return result.get(0);
        }
    }

    @Override
    public User getByEmail(String email) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where email = :email", User.class);
            query = query.setParameter("email", email);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "email", email);
            }
            return result.get(0);
        }
    }

    @Override
    public User getByPhoneNumber(String phoneNumber) {
        try (Session session = sessionFactory.openSession()) {
            Query<User> query = session.createQuery("from User where phoneNumber = :phone", User.class);
            query = query.setParameter("phone", phoneNumber);
            List<User> result = query.list();
            if (result.isEmpty()) {
                throw new EntityNotFoundException("User", "phone", phoneNumber);
            }
            return result.get(0);
        }
    }

    @Override
    public void changeStatusToBlocked(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user.setUserStatus(UserStatus.BLOCKED);
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void changeStatusToActive(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user.setUserStatus(UserStatus.ACTIVE);
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void setUserToAdmin(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user.setUserRole(UserRole.ADMIN);
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void create(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            user.setUserRole(UserRole.USER);
            user.setUserStatus(UserStatus.ACTIVE);
            session.persist(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void update(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.merge(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public void delete(int id) {
        User user = getById(id);
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.remove(user);
            session.getTransaction().commit();
        }
    }

    @Override
    public List<User> getUsersWithHighestRating(int limit) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM User u WHERE u.rating IS NOT NULL ORDER BY u.rating DESC";
            Query<User> query = session.createQuery(hql, User.class);
            query.setMaxResults(limit);
            return query.list();
        }
    }


}
