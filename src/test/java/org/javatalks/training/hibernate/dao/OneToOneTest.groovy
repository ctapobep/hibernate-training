package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.AccessCard
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
    void "OTO without FK constraints"() {
        def card = new AccessCard(code: "EK91234", type: AccessCard.Type.USUAL)
        User user = new User(username: "I'm the One", accessCard: card)

        userDao.save(user).session().flush()
    }

    @Autowired UserDao userDao;
}
