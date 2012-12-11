package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Library
import org.javatalks.training.hibernate.entity.LibraryOwner
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@TransactionConfiguration
@Transactional
class BulkTest {
    @Test
    void "you cannot get much of performance with uni-directional collections"() {
        Library library = Library.createWithBook(10)
        libraryDao.save(library).flushAndClearSession()
    }

    @Test
    void "inverse=false for bidi-collection can use batches"(){
        Library library = new Library()
        libraryDao.save(library)
        libraryDao.addOwnersToLibrary(library, LibraryOwner.create(10))
        libraryDao.session().flush()
    }

    @Test
    void "to-remove"() {
        for(Library lib: Library.create(10)){
            libraryDao.save(lib)
        }
        libraryDao.flushAndClearSession()
        println "[SPARTA!!]"+libraryDao.session().createQuery("select distinct l from Library l join l.books b where id < 10 and b.id in(select id from Book where id < 150)").list()
    }


    @Autowired LibraryDao libraryDao
}
