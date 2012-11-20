package org.javatalks.training.hibernate.dao;

import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.Library;

/** @author stanislav bashkirtsev */
public class LibraryDao extends CrudDao<Library> {
    public LibraryDao(SessionFactory sessionFactory) {
        super(sessionFactory, Library.class);
    }
}
