package org.javatalks.training.hibernate.jdbcdao

import org.javatalks.training.hibernate.entity.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import java.sql.SQLException

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@TransactionConfiguration
@Transactional
class UserJdbcDaoTest {

    @Test
    void saveOrUpdateShouldInsert() {
        User user = new User(username: "Alice from Wonderland");
        sut.saveOrUpdate(user)
        assertReflectionEquals(user, sut.get(user.id))
    }

    @Test
    void getShouldReturnFullObject() {
        User expected = givenSavedUser()
        User actual = sut.get(expected.id)
        assertReflectionEquals(expected, actual)
    }

    @Test
    void deleteShouldNotLeaveRecordsInDb() {
        User user = givenSavedUser()
        sut.delete(user)
        assert sut.get(user.id) == null
    }

    @Test
    void updateShouldChangeAllColumns() throws Exception {
        User alreadySaved = givenSavedUser()
        alreadySaved.username = "I'm a scat man!"
        sut.saveOrUpdate(alreadySaved)
        assertReflectionEquals(alreadySaved, sut.get(alreadySaved.id));
    }

    @Test(expected = SQLException.class)
    public void updateShouldThrowIfIdIsWrong() throws Exception {
        User alreadySaved = givenSavedUser()
        alreadySaved.username = "I'm a scat man!"
        alreadySaved.id = -1L
        sut.saveOrUpdate(alreadySaved)
        assertReflectionEquals(alreadySaved, sut.get(alreadySaved.id));
    }

    /**
     * This test might not be fair because there is an overhead inside of {@link UserJdbcDao#insert(User)}) like logging
     * or generated key returning, but you can try to remove all that stuff to get more accurate results.
     * @throws Exception no one cares
     */
    @Test
    public void batchDemonstration() throws Exception {
        long start = System.currentTimeMillis();
        sut.batchInsert(User.create(10000))
        long batchFinished = System.currentTimeMillis() - start

        start = System.currentTimeMillis()
        for (User user : User.create(10000)) {
            sut.saveOrUpdate(user)
        }
        long insertsFinished = System.currentTimeMillis() - start

        println "JDBC batch took: " + batchFinished
        println "JDBC insert took: " + insertsFinished
    }

    User givenSavedUser() {
        User user = new User(username: "Kot Matthew Rosskin")
        sut.saveOrUpdate(user)
        return user
    }

    @Autowired
    private UserJdbcDao sut;
}
