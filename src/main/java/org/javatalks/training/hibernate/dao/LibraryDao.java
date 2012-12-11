package org.javatalks.training.hibernate.dao;

import org.hibernate.FlushMode;
import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.entity.Library;
import org.javatalks.training.hibernate.entity.LibraryOwner;

import java.security.acl.Owner;
import java.util.List;

/** @author stanislav bashkirtsev */
public class LibraryDao extends CrudDao<Library> {
    public LibraryDao(SessionFactory sessionFactory) {
        super(sessionFactory, Library.class);
    }

    public void addOwnersToLibrary(Library library, List<LibraryOwner> owners){
        session().setFlushMode(FlushMode.MANUAL);
        for(LibraryOwner owner: owners){
            owner.setLibrary(library);
            session().save(owner);
        }
        session().flush();
    }
}
