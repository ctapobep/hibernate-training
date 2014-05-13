package org.javatalks.training.hibernate.dao

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
class PropertiesTest {
    @Test
    void "demonstrate properties"() {
        User original = new User(age: 25,
                biography: "Some day some one may want to make a movie of your life. Make sure it won't go straight " +
                        "to video.",
                avatar: [1, 2, 3, 4, 5, 6],
                username: "username",
                birthday: new Date())
        dao.save(original)
        dao.session().flush()
        dao.session().clear()

        User fromDb = dao.get(original.id)
        dao.session().clear()
        assertReflectionEquals(original, fromDb)
    }

    @Autowired UserDao dao;
}
