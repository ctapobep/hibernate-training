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
    void "you cannot get much of performance with uni-directional collections because it issues 2x queries"() {
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


    @Autowired LibraryDao libraryDao
}
