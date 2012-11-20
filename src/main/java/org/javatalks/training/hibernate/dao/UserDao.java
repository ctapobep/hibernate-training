package org.javatalks.training.hibernate.dao;

import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.User;

/** @author stanislav bashkirtsev */
public class UserDao extends CrudDao<User> {
    public UserDao(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }
}
