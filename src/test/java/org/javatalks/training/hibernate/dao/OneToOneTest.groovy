package org.javatalks.training.hibernate.dao

import org.apache.commons.lang.math.RandomUtils
import org.apache.commons.lang3.RandomStringUtils
import org.hibernate.LazyInitializationException
import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.BookCover
import org.javatalks.training.hibernate.entity.Chapter
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
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
class OneToOneTest {

    @Test
    void "embedded object is saved into the same table even without cascades"() {
        Book book = new Book(title: "Yet another book", cover: new BookCover(color: "blue", hard: true))
        bookDao.save(book).session().flush()

        BookCover cover = jdbcTemplate.queryForObject("select color, hard from book", new BeanPropertyRowMapper<BookCover>(BookCover.class))
        assertReflectionEquals(book.cover, cover)
    }

    @Test
    void "embedded object cannot be lazy"() {
        Book book = new Book(title: "Yet another book", cover: new BookCover(color: "blue", hard: true))
        bookDao.save(book).session().flush()
        bookDao.get(book.id)

        Book fromDb = bookDao.get(book.id)
        bookDao.session().clear()
        assert fromDb.cover == book.cover
    }

    @Test
    void "list of embedded objects (components) should be stored in a separate table without primary key"() {
        Book book = new Book(title: "Yet another book", chapters: chapters())
        bookDao.save(book)
        bookDao.session().flush()

        int amountOfChaptersAfter = jdbcTemplate.queryForInt("select count(*) from chapter where book_id=" + book.id)
        assert amountOfChaptersAfter == book.chapters.size()
    }

    @Test(expected = LazyInitializationException.class)
    void "list of embedded objects is lazy. Let's see what lazy collection is inside"() {
        Book book = givenPersistedBookWithChapters()
        bookDao.session().clear()

        Book fromDb = bookDao.get(book.id)
        bookDao.session().clear()

        fromDb.chapters[0]
    }

    private static Collection<Chapter> chapters() {
        return [
                new Chapter(name: UUID.randomUUID().toString(), pageCount: RandomUtils.nextInt()),
                new Chapter(name: UUID.randomUUID().toString(), pageCount: RandomUtils.nextInt())]
    }

    private Book givenPersistedBookWithChapters() {
        Book book = new Book(title: RandomStringUtils.random(10, UUID.toString()), chapters: chapters())
        bookDao.save(book)
        bookDao.session().flush()
        return book
    }


    @Autowired BookDao bookDao;
    @Autowired JdbcTemplate jdbcTemplate;
}
