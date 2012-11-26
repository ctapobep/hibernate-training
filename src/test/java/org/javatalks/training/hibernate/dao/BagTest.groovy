package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Author
import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.Bookmark
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
class BagTest {

    @Test
    void "unidirectional bag (and thus inverse=false) is not effective at all"(){
        List<Author> authors = [new Author(name: "a1"), new Author(name: "a2")]
        Book book = new Book(title: "with authors", authors: authors)
        bookDao.save(book).session().flush()
        bookDao.session().clear()

        Book fromDb = bookDao.get(book.id)
        fromDb.authors.add(new Author(name: "a3"))
        fromDb.authors.add(new Author(name: "a4"))
        bookDao.session().flush()
        bookDao.session().clear()

        fromDb = bookDao.get(book.id)
        fromDb.authors.add(new Author(name: "a5"))
        bookDao.session().flush()
    }

    @Test
    void "bidi bag with inverse=true is the most effective collection ever!"(){
        Book book = new Book(title: "bags are the best")
        book.addBookmark(new Bookmark("b1", 10))
        bookDao.save(book).session().flush()
        bookDao.session().clear()

        Bookmark bookmark = new Bookmark("b3", 30)
        bookmark.book = book;
        bookDao.session().save(bookmark)
        book = bookDao.get(book.id)
        assert book.bookmarks.size() == 2
    }


    @Autowired BookDao bookDao;
}
