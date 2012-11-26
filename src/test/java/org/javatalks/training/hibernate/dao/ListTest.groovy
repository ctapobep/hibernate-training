package org.javatalks.training.hibernate.dao

import org.hibernate.HibernateException
import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.Chapter
import org.javatalks.training.hibernate.entity.Comment
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
class ListTest {

    @Test
    void "List demonstration"(){
        List<Comment> comments = [new Comment(body: "comment1"), new Comment(body: "comment2")]
        Book book = new Book(title: "with comments", comments: comments)
        bookDao.save(book).session().flush()
        bookDao.session().clear()

        Book fromDb = bookDao.get(book.id)
        fromDb.comments.add(new Comment(body: "comment3"))

        bookDao.session().flush()
    }

    @Test(expected = HibernateException.class)
    void "inverse=true cannot be applied to lists because from 'one' side there is no knowledge about list index"() {
        Book book = new Book(title: "with chapter")
        book.addChapter(new Chapter(name: "ch1"))
        bookDao.save(book).session().flush()
        bookDao.session().clear()

        Chapter chapter = new Chapter(name: "ch2")
        chapter.book = book
        bookDao.session().save(chapter)
        bookDao.session().flush()

        book = bookDao.get(book.id)
        book.chapters.size()// here we get an exception
    }

    @Autowired BookDao bookDao;
}
