package org.javatalks.training.hibernate.dao

import org.hibernate.id.IdentifierGenerationException
import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.Library
import org.javatalks.training.hibernate.entity.Publisher
import org.javatalks.training.hibernate.entity.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@Transactional
class IdGeneratorTest {
    @Test
    void "native inserts records right away on save"() {
        Book book = new Book(title: "I'm inserted right away on save()")
        bookDao.save(book)
        if (isIdentityGenerator()) {
            assert book.id == jdbc.queryForObject("select id from book where title = ?", Long.class, book.title)
        } else if (isPostgres()) {
            assert book.id == jdbc.queryForObject("select lastval()", Long.class)
        } else {
            assert false, "Database was not recognized to determine its native generator"
        }
    }

    @Test(expected = IdentifierGenerationException.class)
    void "assigned throws exception if you actually didn't assign"() {
        userDao.save(new User(username: "I'll throw up because no one assigned ID to me :~"))
    }

    @Test
    void "assigned uses the same value you specified"() {
        assert userDao.get(12300L) == null, "Make sure you don't have records in the database before you run this test"
        userDao.save(new User(id: 12300L, username: "I'll throw up because no one assigned ID to me :~"))
    }

    @Test
    void "foreign uses another property as an ID"() {
        User owner = new User(id: 100500L, username: "Da Boss")
        Library lib = new Library(name: "Yes, my master", owner: owner)
        assert libraryDao.get(lib.name.hashCode()) == null, "Make sure you don't have records in the database before you run this test"
        libraryDao.save(lib)
        assert lib.id == lib.owner.id
    }

    @Test
    void "composite keys"() {
        Publisher expected = new Publisher(id: new Publisher.Id(name: "Manning", city: "Oz"))
        publisherDao.saveOrUpdate(expected) //select is generated to figure out whether it's a new object or existing
        Publisher actual = publisherDao.get(new Publisher.Id(name: "Manning", city: "Oz"))
        assertReflectionEquals(expected, actual)
    }

    private boolean isIdentityGenerator() {
        return ["mysql", "hsqldb"].contains(dbname)
    }

    private boolean isPostgres() {
        return "postgres" == dbname
    }

    @Autowired BookDao bookDao;
    @Autowired UserDao userDao;
    @Autowired LibraryDao libraryDao;
    @Autowired PublisherDao publisherDao;
    @Autowired JdbcTemplate jdbc;
    @Value("\${dbname}") String dbname;
}
