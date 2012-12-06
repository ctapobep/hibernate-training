package org.javatalks.training.hibernate.dao

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
class EntityOptimizationTest {

    @Test
    void "update=false does not update the field"() {
        User original = new User(name: "original name", base64avatar: "original avatar")
        dao.save(original).flushSession()

        original.base64avatar = "updated avatar"
        original.name = "updated name"
        dao.flushAndClearSession()

        User fromDb = dao.get(original.id)
        assert fromDb.name == "updated name"
        assert fromDb.base64avatar != "updated avatar"
    }

    @Autowired UserDao dao;

}
