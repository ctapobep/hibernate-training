package org.javatalks.training.hibernate.dao

import org.apache.commons.lang3.RandomStringUtils
import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.Library
import org.javatalks.training.hibernate.entity.User
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
class OneToOneTest {
    @Test
    void "OTO doesn't use lazy loading by default"() {
        User user = new User(username: "I'm not lazy!")
        Library library = new Library(name: "I have a non-lazy owner", owner: user)
        libraryDao.save(library)
    }

    @Test
    void "OTM should have a main side"() {
        User user = new User(username: "Who's the man?")
        user.setBooks(givenPersistedBooks(10))
        usersDao.saveOrUpdate(user)
    }

    private Collection<Book> givenPersistedBooks(int amount = 1) {
        Set<Book> books = new HashSet<>(amount)
        for (int i = 0; i < amount; i++) {
            books.add(givenPersistedBook())
        }
        return books
    }

    private Book givenPersistedBook() {
        Book book = new Book(title: RandomStringUtils.random(10, UUID.toString()))
        bookDao.save(book)
        return book
    }

    private User givenPersistedUser() {
        User user = new User(username: "I'm not lazy!")
        usersDao.save(user)
        return user
    }

    @Autowired BookDao bookDao;
    @Autowired UserDao usersDao;
    @Autowired LibraryDao libraryDao;
}
