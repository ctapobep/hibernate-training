package org.javatalks.training.hibernate.dao;

import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.User;

import java.util.List;

/** @author stanislav bashkirtsev */
public class BookDao extends CrudDao<User> {
    public BookDao(SessionFactory sessionFactory) {
        super(sessionFactory, User.class);
    }


    public List<User> all(){
        return (List<User>) session().createQuery("from User").list();
    }
}
