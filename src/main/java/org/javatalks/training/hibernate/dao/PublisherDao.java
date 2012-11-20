package org.javatalks.training.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.Publisher;

/** @author stanislav bashkirtsev */
public class PublisherDao {

    public PublisherDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public Publisher get(Publisher.Id id) {
        return (Publisher) session().get(Publisher.class, id);
    }

    public void saveOrUpdate(Publisher entity) {
        session().saveOrUpdate(entity);
    }

    /**
     * This method will be handy for both this class and its test.
     *
     * @return the session currently associated with the tread
     */
    protected Session session() {
        return sessionFactory.getCurrentSession();
    }

    private final SessionFactory sessionFactory;

}
