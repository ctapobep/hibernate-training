package org.javatalks.training.hibernate.dao;

import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.Book;

import java.util.List;

/** @author stanislav bashkirtsev */
public class BookDao extends CrudDao<Book> {
    public BookDao(SessionFactory sessionFactory) {
        super(sessionFactory, Book.class);
    }


    public List<Book> all(){
        return (List<Book>) session().createQuery("from Book").list();
    }
}
