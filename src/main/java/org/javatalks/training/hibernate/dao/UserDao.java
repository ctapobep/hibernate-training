package org.javatalks.training.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.User;

/** @author stanislav bashkirtsev */
public class UserDao implements Crud<User> {
    public UserDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void save(User entity) {
        session().save(entity);
    }

    public void saveOrUpdate(User entity){
        session().saveOrUpdate(entity);
    }

    @Override
    public void update(User entity) {
        session().update(entity);
    }

    @Override
    public User get(long id) {
        return (User) session().get(User.class, id);
    }

    public User load(long id) {
        return (User) session().load(User.class, id);
    }

    public User merge(User toBeMergedAndThrownAway){
        return (User) session().merge(toBeMergedAndThrownAway);
    }

    @Override
    public void delete(User entity) {
        session().delete(entity);
    }

    public long count(){
        return (long) session().createQuery("select count(*) from User").uniqueResult();
    }

    /**
     * This method will be handy for both this class and its test.
     * @return the session currently associated with the tread
     */
    Session session() {
        return sessionFactory.getCurrentSession();
    }

    private final SessionFactory sessionFactory;
}
