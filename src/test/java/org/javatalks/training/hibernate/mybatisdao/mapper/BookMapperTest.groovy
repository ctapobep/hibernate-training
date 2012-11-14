package org.javatalks.training.hibernate.mybatisdao.mapper

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
class BookMapperTest {
    @Test
    void "insert() should store and get() should return stored Book"() {
        Book expected = givenBookSaved()
        assertReflectionEquals(sut.get(expected.id), expected)
    }

    @Test
    void "update() should update all the fields"(){
        Book expected = givenBookSaved()
        expected.title = "Pedobear in Wild Nature"
        sut.update(expected)
        assertReflectionEquals(sut.get(expected.id), expected)
    }

    @Test
    void "delete() should remove the record"(){
        Book expected = givenBookSaved()
        sut.delete(expected)
        assert sut.get(expected.id) == null
    }

    private Book givenBookSaved() {
        Book book = new Book(title: "Pink Unicorns")
        sut.insert(book)
        return book
    }

    @Autowired
    private BookMapper sut;
}