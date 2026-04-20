// Author: W2151955/ 20241937 / Lakindu Jayathilaka
package com.smartcampus.repository;

import com.smartcampus.model.User;
import com.smartcampus.util.HibernateUtil;
import org.hibernate.Session;

public class UserRepository extends BaseRepository<User, Long> { // db access for users
    public UserRepository() {
        super(User.class);
    }

    public User findByUsername(String username) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("from User where username = :un", User.class)
                    .setParameter("un", username)
                    .uniqueResult();
        }
    }
}
