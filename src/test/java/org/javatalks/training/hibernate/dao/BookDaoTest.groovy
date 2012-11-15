package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Book
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@TransactionConfiguration
@Transactional
class BookDaoTest {
    @Test
    void "save() sets the ID"() {
        Book book = new Book(title: "Hibernate.. At last")
        sut.insert(book)//now book is managed by hibernate, it's persisted
        assert book.id != null
    }

    @Test
    void "get() returns same object as was stored"() {
        Book book = new Book(title: "Hibernate.. At last")
        sut.insert(book) //now book is managed by hibernate, it's persisted
        assertReflectionEquals(book, sut.get(book.id))
    }

    @Autowired
    private BookDao sut;
}
