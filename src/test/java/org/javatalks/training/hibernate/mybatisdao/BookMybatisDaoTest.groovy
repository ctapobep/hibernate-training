package org.javatalks.training.hibernate.mybatisdao

import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.User
import org.javatalks.training.hibernate.mybatisdao.mapper.UserMapper
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
@TransactionConfiguration(defaultRollback = false)
@Transactional
class BookMybatisDaoTest {
    @Test
    void "insert() should store and get() should return stored Book"() {
        Book expected = givenBookSaved()
        assert expected.id != null
        assertReflectionEquals(expected, sut.get(expected.id))
    }

    @Test
    void "inserting collection should generate IDs for each of them"() {
        List<Book> books = [new Book(title: "title1"), new Book(title: "title2")]
        sut.insertOrUpdateBooks(books)
        assert books.every({ it.id != null })
    }

    @Test
    void "get should return Book without author"() {
        def expected = givenBookWithAuthorSaved()
        expected.author = null
        assertReflectionEquals(expected, sut.get(expected.id))
    }

    @Test
    void "getWithAuthor() should return initialized user"(){
        def expected = givenBookWithAuthorSaved()
        def actual = sut.getWithAuthor(expected.id)
        expected.author.booksByDao = []
        assertReflectionEquals(expected, actual)
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
    private Book givenBookWithAuthorSaved() {
        User author = givenSavedUser()
        Book book = new Book(title: "Pink Unicorns")
        book.author = author
        author.addBook(book)
        userMapper.insert(author)
        return book
    }

    private User givenSavedUser(){
        User user = new User(username: "username")
        userMapper.insert(user)
        return user
    }

    @Autowired
    private BookMybatisDao sut;
    @Autowired
    private UserMapper userMapper;
}
