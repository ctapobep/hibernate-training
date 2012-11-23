package org.javatalks.training.hibernate.dao

import org.hibernate.LazyInitializationException
import org.javatalks.training.hibernate.entity.AccessCard
import org.javatalks.training.hibernate.entity.AccountForPaidUsers
import org.javatalks.training.hibernate.entity.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.jdbc.core.BeanPropertyRowMapper
import org.springframework.jdbc.core.JdbcTemplate
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
class OneToOneTest {

    /**
     * This kind of relationship is bidirectional, both tables have PKs which have the same values. In child table we
     * have FK constraint access_card.id references users.id. This is a pretty tricky way of mapping associations since
     * we have to be careful in order not to get into awkward situation when IDs become different in some way or access
     * card doesn't simply get wrong user id. We'd rather be explicit instead and have column user_id or card_id.
     */
    @Test
    void "OTO with shared PK"() {
        def card = new AccessCard(code: "EK91234", type: AccessCard.Type.USUAL)
        User user = new User(username: "I'm the One", accessCard: card)

        userDao.save(user).session().flush()
        def fromDb = jdbc.queryForObject(
                "select * from access_card join users on access_card.id=users.id where users.id=" + user.id,
                new BeanPropertyRowMapper<AccessCard>(AccessCard.class))

        assertReflectionEquals(card, fromDb)
    }

    /**
     * Hibernate can't know whether there is an associated card or it's just null. In order to figure that out,
     * Hibernate would need to issue additional query which doesn't actually differ much from retrieving the whole
     * association, that's why in this case we don't have laziness.
     */
    @Test
    void "OTO with shared PK cannot be lazy"() {
        User user = userWithCard()
        user.account = new AccountForPaidUsers(user: user, number: "123", availableMoney: 10.0)
        userDao.save(user).session().flush()
        userDao.session().clear()

        User fromDb = userDao.get(user.id)
        fromDb.books = user.books//get rid of noise
        fromDb.accessCard = user.accessCard//get rid of noise
        userDao.session().clear()

        assertReflectionEquals(user.account, fromDb.account)
    }

    /**
     * Weeell. Actually OTO can be lazy, but you need to configure constraint="true" so that Hibernate knows exactly
     * that association <b>always</b> exists. So now Hibernate can create proxy because it's sure the association will
     * be loaded from DB.
     */
    @Test(expected = LazyInitializationException.class)
    void "OTO with shared PK is not lazy by default"() {
        User user = userWithCard()
        userDao.save(user).session().flush()
        userDao.session().clear()

        User fromDb = userDao.get(user.id)
        fromDb.books = user.books//get rid of lazy collection
        userDao.session().clear()

        assertReflectionEquals(user, fromDb)
    }

    private static User userWithCard() {
        AccessCard card = new AccessCard(code: "EK91234", type: AccessCard.Type.USUAL)
        User user = new User(username: "I'm the One", accessCard: card)
        return user
    }

    @Autowired UserDao userDao;
    @Autowired JdbcTemplate jdbc;
}
