package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Book
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.jdbc.core.JdbcTemplate
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
class IdGeneratorTest {
    @Test
    void "native inserts records right away on save"() {
        Book book = new Book(title: "I'm inserted right away on save()")
        bookDao.save(book)
        if (isIdentityGenerator()) {
            assert book.id == jdbc.queryForLong("select id from book where title = ?", "I'm inserted right away on save()")
        } else if (isPostgres()) {
            assert book.id == jdbc.queryForLong("select lastval()")
        } else {
            assert false, "Database was not recognized to determine its native generator"
        }
    }

    private boolean isIdentityGenerator() {
        return ["mysql", "hsqldb"].contains(dbname)
    }

    private boolean isPostgres() {
        return "postgres" == dbname
    }

    @Autowired
    private BookDao bookDao;
    @Autowired
    private JdbcTemplate jdbc;
    @Value("\${dbname}")
    private String dbname;
}
