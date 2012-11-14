package org.javatalks.training.hibernate.springdao

import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.User
import org.javatalks.training.hibernate.springdao.util.LazyInitializationException
import org.javatalks.training.hibernate.springdao.util.WrongCascadeDirectionException
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
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
class BookSpringJdbcDaoTest {
    @Test
    void "saveOrUpdate() should insert new record"() {
        Book lib = new Book(title: "Peace and War", author: givenSavedUser("Lev Tolstoy"))
        sut.saveOrUpdate(lib)
        lib.author = null //we don't want to compare author here
        assertReflectionEquals(lib, sut.get(lib.id),)
    }

    @Test(expected = WrongCascadeDirectionException.class)
    void "saveOrUpdate() had to throw because author is transient"() {
        User author = new User(username: "Mr. Nobody")
        Book lib = new Book(title: "The Book that Didn't Exist", author: author)
        sut.saveOrUpdate(lib)
        assertReflectionEquals(lib, sut.get(lib.id))
    }

    @Test
    void "get() should return object without associations"() {
        Book expected = givenSavedBook()
        Book actual = sut.get(expected.id)
        expected.author = null //we don't want to compare author
        assertReflectionEquals(expected, actual)
    }

    @Test
    void "getWithAuthor() should return object with filled Author"() {
        Book expected = givenSavedBook()
        Book actual = sut.getWithAuthor(expected.id)
        actual.author.setBooksByDao(expected.author.books)//we expect that User#books is lazy when returned by DAO
        assertReflectionEquals(expected, actual)
    }

    @Test(expected = LazyInitializationException.class)
    void "getWithAuthor() should return Author with lazy collection of books"() {
        Book expected = givenSavedBook()
        Book actual = sut.getWithAuthor(expected.id)
        actual.author.books.size()//should throw exception here
    }

    @Test
    void "delete should not leave records in DB"() {
        Book lib = givenSavedBook()
        sut.delete(lib)
        assert sut.get(lib.id) == null
    }

    @Test
    void "ofAuthor() should fetch books"() {
        User user = new User(username: "Guy with a good fantasy")
        user.addBook(new Book(title: "Final Fantasy1"))
        user.addBook(new Book(title: "Final Fantasy2"))
        user.addBook(new Book(title: "Final Fantasy3"))
        userDao.saveOrUpdate(user)

        List<Book> actualBooks = sut.ofAuthor(user)
        assert actualBooks.containsAll(user.books)
        assert user.books.containsAll(actualBooks)
    }

    @Test
    void "ofAuthor() should return books with passed author"() {
        User user = new User(username: "Guy with a good fantasy")
        user.addBook(new Book(title: "Final Fantasy1"))
        userDao.saveOrUpdate(user)

        List<Book> actualBooks = sut.ofAuthor(user)
        assert actualBooks.each {it.author == user}
    }

    @Test
    void "delete should not cascade to Author"() {
        Book lib = givenSavedBook()
        sut.delete(lib)
        assert userDao.get(lib.author.id) != null
    }

    @Test
    void "update should change all columns"() throws Exception {
        Book alreadySaved = givenSavedBook()
        alreadySaved.title = "Comics for Retarded Children"
        alreadySaved.author = givenSavedUser("Retarded Child")
        sut.saveOrUpdate(alreadySaved)
        alreadySaved.author = null

        assertReflectionEquals(alreadySaved, sut.get(alreadySaved.id));
    }

    @Test(expected = DataIntegrityViolationException.class)
    void "saveOrUpdate() should throw if ID was wrong"() throws Exception {
        Book alreadySaved = givenSavedBook()
        alreadySaved.title = "Comics for Retarded Children"
        alreadySaved.id = -1L
        sut.saveOrUpdate(alreadySaved)
    }

    private Book givenSavedBook() {
        Book book = new Book(title: "The National Ukrainian Book (NUL)", author: givenSavedUser("Lev Tolstoy"))
        sut.saveOrUpdate(book)
        return book
    }

    private User givenSavedUser(String username) {
        User user = new User(username: username)
        userDao.saveOrUpdate(user)
        return user
    }

    @Autowired
    private BookSpringJdbcDao sut;
    @Autowired
    private UserSpringJdbcDao userDao;
}
