package org.javatalks.training.hibernate.springdao

import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.User
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
class UserSpringJdbcDaoTest {

    @Test
    void saveOrUpdateShouldInsert() {
        User user = new User(username: "Alice from Wonderland");
        sut.saveOrUpdate(user)
        assertUsersEqualIgnoringAssociations(user, sut.get(user.id))
    }

    @Test
    void "get() should fetch all private fields"() {
        User expected = givenSavedUser()
        User actual = sut.get(expected.id)
        assertUsersEqualIgnoringAssociations actual, expected
    }

    @Test
    void deleteShouldNotLeaveRecordsInDb() {
        User user = givenSavedUser()
        sut.delete(user)
        assert sut.get(user.id) == null
    }

    @Test
    void "saveOrUpdate() should update all the columns. With no books"() throws Exception {
        User alreadySaved = givenSavedUser()
        alreadySaved.username = "I'm a scat man!"
        sut.saveOrUpdate(alreadySaved)

        assertUsersEqualIgnoringAssociations alreadySaved, sut.get(alreadySaved.id)
    }

    @Test
    void "save should update authors of books"() {
        Book book = givenSavedBook()
        User user = givenSavedUser("Plagiarist")
        user.addBook(book)
        sut.saveOrUpdate(user)

        assert bookDao.getWithAuthor(book.id).authorId == user.id
    }

    @Test
    void "update of users should insert new books"() {
        User user = givenSavedUser("Poor Author")
        user.addBook(new Book(title: "About Poor People"))
        user.addBook(new Book(title: "Poor People against Rich Conquerors"))
        sut.saveOrUpdate(user)

        assert bookDao.ofAuthor(user).containsAll(user.getBooks())
    }

    @Test
    void "getWithBooks() should return User with books"() {
        User user = new User(
                username: "user",
                books: [new Book(title: "1"), new Book(title: "2")]
        )
        sut.saveOrUpdate(user)

        assertReflectionEquals(sut.getWithBooks(user.getId()), user)
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void updateShouldThrowIfIdIsWrong() throws Exception {
        User alreadySaved = givenSavedUser()
        alreadySaved.username = "I'm a scat man!"
        alreadySaved.id = -1L
        sut.saveOrUpdate(alreadySaved)
        assertReflectionEquals(alreadySaved, sut.get(alreadySaved.id));
    }

    private User givenSavedUser() {
        User user = new User(username: "Kot Matthew Rosskin")
        sut.saveOrUpdate(user)
        return user
    }

    private User givenSavedUser(String username) {
        User user = new User(username: username)
        sut.saveOrUpdate(user)
        return user
    }

    private Book givenSavedBook() {
        Book book = new Book(title: "Generation P", author: givenSavedUser("Pelevin"))
        bookDao.saveOrUpdate(book)
        return book
    }

    private void assertUsersEqualIgnoringAssociations(User actual, User expected) {
        actual.setBooksByDao(expected.books)
        assertReflectionEquals(expected, actual)
    }

    @Autowired
    private UserSpringJdbcDao sut;
    @Autowired
    private BookSpringJdbcDao bookDao;
}
