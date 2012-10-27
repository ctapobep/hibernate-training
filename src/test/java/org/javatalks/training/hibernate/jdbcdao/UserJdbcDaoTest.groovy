package org.javatalks.training.hibernate.jdbcdao

import org.javatalks.training.hibernate.entity.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner

import java.sql.SQLException

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals
/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
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
    public void updateShouldChangeAllColumns() throws Exception {
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

    User givenSavedUser() {
        User user = new User(username: "Kot Matthew Rosskin")
        sut.saveOrUpdate(user)
        return user
    }

    @Autowired
    private UserJdbcDao sut;
}
