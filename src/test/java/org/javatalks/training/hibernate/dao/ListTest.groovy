package org.javatalks.training.hibernate.dao

import org.hibernate.HibernateException
import org.javatalks.training.hibernate.entity.bag.Book
import org.javatalks.training.hibernate.entity.bag.Chapter
import org.javatalks.training.hibernate.entity.bag.Comment
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import static org.junit.Assert.assertTrue
import static org.junit.Assert.fail
import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@TransactionConfiguration
@Transactional
class ListTest {

    @Test
    void "List demonstration"(){
        List<Comment> comments = [new Comment(body: "comment1"), new Comment(body: "comment2")]
        Book original = new Book(title: "with comments", comments: comments)
        bookDao.save(original).flushAndClearSession()

        Book fromDb = bookDao.get(original.id)
        assertReflectionEquals(fromDb, original)
    }

    @Test
    void "inverse=true cannot be applied to lists because from 'one' side there is no knowledge about list index"() {
        Book book = new Book(title: "with chapter")
        book.addChapter(new Chapter(name: "ch1"))
        bookDao.save(book).flushAndClearSession()

        book = bookDao.get(book.id)
        try {
            book.chapters.size()// here we get an exception
            fail("Exception should have been thrown")
        } catch (HibernateException e) {
            assertTrue(e.getMessage().contains("null index column for collection"))
        }
    }

    @Autowired BookDao bookDao;
}
