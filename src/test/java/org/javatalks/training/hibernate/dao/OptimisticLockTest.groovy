package org.javatalks.training.hibernate.dao

import org.hibernate.StaleObjectStateException
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
class OptimisticLockTest {

    @Test(expected = StaleObjectStateException.class)
    void "version based locking"() {
        User original = new User(name: "user1")
        dao.save(original).flushAndClearSession()

        User fromDb = dao.get(original.id)
        fromDb.name = "updated value"
        dao.flushAndClearSession()

        dao.saveOrUpdate(original)
        dao.flushAndClearSession()
    }

    @Autowired UserDao dao;

}
