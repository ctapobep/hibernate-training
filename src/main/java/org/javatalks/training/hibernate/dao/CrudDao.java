package org.javatalks.training.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 * Contains all the basic and the most primitive operations that are common for all the entities so that we don't
 * duplicate the same thing over and over again. You can either extend this class and use it as basic or use it as a
 * delegate.
 *
 * @author stanislav bashkirtsev
 */
public class CrudDao<T> implements Crud<T> {
    public CrudDao(SessionFactory sessionFactory, Class<T> entityClass) {
        this.sessionFactory = sessionFactory;
        this.entityClass = entityClass;
    }

    @Override
    public CrudDao<T> save(T entity) {
        session().save(entity);
        return this;
    }

    @Override
    public CrudDao<T> update(T entity) {
        session().update(entity);
        return this;
    }

    @Override
    public T get(long id) {
        //noinspection unchecked
        return (T) session().get(entityClass, id);
    }

    public T load(long id) {
        //noinspection unchecked
        return (T) session().load(entityClass, id);
    }

    public T merge(T toBeMergedAndThrownAway) {
        //noinspection unchecked
        return (T) session().merge(toBeMergedAndThrownAway);
    }

    public void saveOrUpdate(T entity) {
        session().saveOrUpdate(entity);
    }

    @Override
    public Crud<T> delete(T entity) {
        session().delete(entity);
        return this;
    }

    public long count() {
        return (long) session().createQuery("select count(*) from " + entityClass.getSimpleName()).uniqueResult();
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
    private final Class<T> entityClass;
}
