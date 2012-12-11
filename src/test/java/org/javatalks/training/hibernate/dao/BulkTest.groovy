package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Library
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
    void "todo"() {
        Library library = Library.createWithBook(10)
        libraryDao.save(library).flushAndClearSession()
    }

    @Autowired LibraryDao libraryDao
}
