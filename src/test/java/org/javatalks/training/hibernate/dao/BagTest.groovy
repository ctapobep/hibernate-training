package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Appendix
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
    void "unidirectional bag (and thus inverse=false) issues update for every its elements"() {
        Book book = new Book(title: "with authors", authors: [new Author(name: "a1"), new Author(name: "a2")])
        bookDao.save(book).session().flush()
    }

    @Test
    void "inverse=false issues delete if element is removed from collection (unless it has not-null=true)"() {
        Book book = new Book(title: "with authors", authors: [new Author(name: "a1"), new Author(name: "a2")])
        bookDao.save(book).flushAndClearSession()

        Book fromDb = bookDao.get(book.id)
        fromDb.authors.remove(0)
        bookDao.flushSession()
    }

    @Test
    void "bag does not duplicate elements on DB level"() {
        Book original = new Book(title: "with authors", authors: [new Author(name: "a1"), new Author(name: "a2")])
        bookDao.save(original).session().flush()
        original.authors.add(original.authors[0])//adding an element that's already there
        bookDao.flushAndClearSession()

        Book fromDb = bookDao.get(original.id)
        assert fromDb.authors.size() == original.authors.size() - 1
    }

    @Test
    void "unidirectional. changing the owner of element simply changes book_id in author table"() {
        Book original = new Book(title: "with authors", authors: [new Author(name: "a1"), new Author(name: "a2")])
        bookDao.save(original).session().flush()

        Book newBook = new Book(title: "stealing an author")
        newBook.authors.add(original.authors[0]) //now the whole association will be saved for this new book
        bookDao.save(newBook).flushAndClearSession()

        Book originalFromDb = bookDao.get(original.id)
        assert originalFromDb.authors.size() == original.authors.size() - 1
    }

    @Test
    void "bidi OTM bag with inverse=true is the most effective collection ever!"() {
        Book book = new Book(title: "bags are the best")
        book.addBookmark(new Bookmark("b1", 10))
        bookDao.save(book).flushAndClearSession()

        Bookmark bookmark = new Bookmark(name: "b3", pageNumber: 30, book: book)
        bookDao.session().save(bookmark)//only a single insert, no other queries
        bookDao.flushAndClearSession()
        book = bookDao.get(book.id)
        assert book.bookmarks.size() == 2
    }

    @Test
    void "bidi MTO with joined table uses a separate table"() {
        Book book = new Book(title: "bags are the best")
        Appendix appendix = new Appendix(name: "a1", book: book)
        book.appendixes.add(appendix)
        bookDao.save(book).flushAndClearSession()

        Book fromDb = bookDao.get(book.id)
        assert fromDb.appendixes == book.appendixes
    }

    @Test
    void "bidi MTM with bags"() {

    }

    @Autowired BookDao bookDao;
}
