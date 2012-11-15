package org.javatalks.training.hibernate.dao;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.Book;

/** @author stanislav bashkirtsev */
public class BookDao implements Crud<Book> {
    public BookDao(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void insert(Book entity) {
        session().save(entity);
    }

    @Override
    public void update(Book entity) {
        session().update(entity);
    }

    @Override
    public Book get(long id) {
        return (Book) session().get(Book.class, id);
    }

    @Override
    public void delete(Book entity) {
        session().delete(entity);
    }

    private Session session() {
        return sessionFactory.getCurrentSession();
    }

    private final SessionFactory sessionFactory;
}
