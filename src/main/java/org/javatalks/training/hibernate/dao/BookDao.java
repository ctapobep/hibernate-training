package org.javatalks.training.hibernate.dao;

import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.Book;

/** @author stanislav bashkirtsev */
public class BookDao extends CrudDao<Book> {
    public BookDao(SessionFactory sessionFactory) {
        super(sessionFactory, Book.class);
    }
}
