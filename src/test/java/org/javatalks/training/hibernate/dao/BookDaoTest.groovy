package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Book
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
@TransactionConfiguration(defaultRollback = false)
@Transactional
class BookDaoTest {
    @Test
    void "save() sets the ID"() {
        Book book = new Book(title: "Hibernate.. At last")
        bookDao.insert(book)//now book is managed by hibernate, it's persisted
        assert book.id != null
    }

    @Autowired
    private BookDao bookDao;
}
