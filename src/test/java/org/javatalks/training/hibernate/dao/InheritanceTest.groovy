package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.Author
import org.javatalks.training.hibernate.entity.Personnel
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
class InheritanceTest {

    @Test
    void "fetching all subclasses by fetching super class"() {
        Author author = new Author(name: "author", rating: 5)
        Personnel personnel = new Personnel(name: "personnel", position: "pj for reading advertisement")
        dao.save(author)
        dao.save(personnel).flushAndClearSession()

        List<User> users = dao.all()
        assert users.size() == 2
        assertReflectionEquals(author, users[0])
        assertReflectionEquals(personnel, users[1])

//        cleanup after test
        dao.delete(users[0])
        dao.delete(users[1])
    }

    @Autowired UserDao dao;

}
