package org.javatalks.training.hibernate.jdbcdao

import org.javatalks.training.hibernate.entity.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
class UserJdbcDaoTest {
    @Test
    void testSaveOrUpdate() {
        User user = new User(username: "Diadia Fiodor");
        sut.saveOrUpdate(user)
    }

    @Test
    void testGet() {

    }

    @Test
    void testDelete() {

    }

    @Autowired
    private UserJdbcDao sut;
}
