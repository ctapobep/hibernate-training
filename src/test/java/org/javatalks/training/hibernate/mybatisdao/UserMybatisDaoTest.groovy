package org.javatalks.training.hibernate.mybatisdao

import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.User
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
class UserMybatisDaoTest {
    @Test
    void "insert() should store and get() should return stored User"() {
        User expected = givenUserSaved()
        assert expected.id != null
        assertReflectionEquals(sut.get(expected.id), expected)
    }

    @Test
    void "get() should return User with no books"() {
        def expected = givenUserWithBooksSaved()
        expected.books.clear()
        assertReflectionEquals(expected, sut.get(expected.id))
    }

    @Test
    void "getWithBooks() should return initialized books"() {
        def expected = givenUserWithBooksSaved()
        assertReflectionEquals(expected, sut.getWithBooks(expected.getId()))
    }

    @Test
    void "update() should update all the fields"() {
        User expected = givenUserSaved()
        expected.username = "new username"
        sut.update(expected)
        assertReflectionEquals(sut.get(expected.id), expected)
    }

    @Test
    void "delete() should remove the record"() {
        User expected = givenUserSaved()
        sut.delete(expected)
        assert sut.get(expected.id) == null
    }

    private User givenUserSaved() {
        User user = new User(username: "username")
        sut.insert(user)
        return user
    }

    private User givenUserWithBooksSaved() {
        User user = new User(username: "Classical User",
                books: [new Book(title: "Romantic Book"), new Book(title: "Epic Book")]
        )
        sut.insert(user)
        return user
    }

    @Autowired
    private UserMybatisDao sut;
}
