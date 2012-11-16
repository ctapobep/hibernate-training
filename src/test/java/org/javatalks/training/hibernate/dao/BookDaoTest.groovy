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
    void "save() sets the ID since generator=native is used"() {
        Book book = new Book(title: "I need an ID to be generated and set!")
        sut.insert(book)
        assert book.id != null
    }

    @Test
    void "get() returns same reference to object that was stored because it's taken from session-level cache"() {
        Book saved = new Book(title: "I need to be stored and retrieved from session level cache!")
        sut.insert(saved)
        Book fromCache = sut.get(saved.id)
        assert fromCache.is(saved)
    }

    @Test
    void "get() should load the same entity from DB because it was evicted from session cache"() {
        Book saved = new Book(title: "I need to be loaded from DB because I was removed from session level cache!")
        sut.insert(saved)
        sut.session().evict(saved)

        Book loadedAgainFromDb = sut.get(saved.id)
        assert !saved.is(loadedAgainFromDb)
        assertReflectionEquals(saved, loadedAgainFromDb)
    }


    @Autowired
    private BookDao sut;
}
