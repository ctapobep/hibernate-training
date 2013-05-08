package org.javatalks.training.hibernate.dao

import org.hibernate.LazyInitializationException
import org.javatalks.training.hibernate.entity.*
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
    void "OTO with shared PK is not lazy by default, but there are means"() {
        User user = userWithCard()
        userDao.save(user).session().flush()
        userDao.session().clear()

        User fromDb = userDao.get(user.id)
        userDao.session().clear()
        fromDb.accessCard.code//throws
    }

    @Test(expected = LazyInitializationException.class)
    void "OTO foreign key association (via MTO) has additional column and thus lazy by default"() {
        User user = userWithCard()
        user.discountCard = new DiscountCard(expiry: new Date(), number: "NU07")
        userDao.save(user).session().flush()
        userDao.session().clear()

        User fromDb = userDao.get(user.id)
        userDao.session().clear()
        fromDb.discountCard.number
    }

    @Test
    void "MTO with inverse OTO gives access to main side from secondary side"() {
        User user = userWithCard()
        user.passport = new Passport(issuer: "Some cool organization")
        userDao.save(user).session().flush()
        userDao.session().clear()

        User fromDb = userDao.get(user.id)
        assert fromDb.passport.user.is(fromDb)
    }

    @Test
    void "MTO with inverse OTO gives access to main side from secondary side, but it's never lazy"() {
        User user = userWithCard()
        user.passport = new Passport(issuer: "Some cool organization")
        userDao.save(user).session().flush()
        userDao.session().clear()

        Passport fromDb = userDao.session().get(Passport.class, user.passport.id) as Passport
        assert fromDb.user == user
    }

    @Test
    void "MTO with inverse OTO may have nulls from any side"() {
        Passport passport = new Passport(issuer: "Some cool organization")
        userDao.session().save(passport)
        userDao.session().flush()
        userDao.session().clear()

        Passport fromDb = userDao.session().get(Passport.class, passport.id) as Passport
        assert fromDb.user == null
    }

    /**
     * This kind of association does not guarantee consistency. You may find yourself in a situation when user1 has pc1,
     * and in the same time you can have pc1 having user2. In order to guarantee consistency, you need to have a
     * separate join table.
     */
    @Test
    void "MTO from both sides demo"() {
        User user = userWithCard()
        RentedPc pc = new RentedPc(user: user)
        user.rentedPc = pc
        userDao.session().save(pc)
        userDao.session().flush()
        userDao.session().clear()

        User fromDb = userDao.get(user.id)
        assertReflectionEquals(user, fromDb)
    }

    @Test
    void "OTO with joined table demonstration"() {
        User user = userWithCard()
        userDao.save(user).session().flush()

        user.reservedDesk = new ReservedDesk(userReserved: user, number: 12)
        userDao.session().flush()
        assert jdbc.queryForInt("select count(*) from user_reserved_desk where user_id=" + user.id + " and reserved_desk_id=" + user.reservedDesk.id) == 1
    }

    private static User userWithCard() {
        AccessCard card = new AccessCard(code: "EK91234", type: AccessCard.Type.USUAL)
        User user = new User(username: "I'm the One", accessCard: card)
        return user
    }

    @Autowired UserDao userDao;
    @Autowired JdbcTemplate jdbc;
}
