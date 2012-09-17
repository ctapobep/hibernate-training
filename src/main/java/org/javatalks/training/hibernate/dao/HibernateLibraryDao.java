package org.javatalks.training.hibernate.dao;

import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Library;

/** @author stanislav bashkirtsev */
public class HibernateLibraryDao implements Crud<Library> {
    private final SessionFactory sessionFactory;

    public HibernateLibraryDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public void saveOrUpdate(Library entity) {
        sessionFactory.getCurrentSession().saveOrUpdate(entity);
    }

    public Library get(long id) {
        return (Library) sessionFactory.getCurrentSession().get(Library.class, id);
    }

    public void delete(Library entity) {
        sessionFactory.getCurrentSession().delete(entity);
    }
}
