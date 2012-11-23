package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.AccessCard
import org.javatalks.training.hibernate.entity.Book
import org.javatalks.training.hibernate.entity.Library
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
class BidirectionalAssociationTest {
    @Test
    void "OTO doesn't use lazy loading by default"() {
        User user = givenUser()
        Library library = new Library(name: "I have a non-lazy owner", owner: user)
        libraryDao.save(library)
    }

    /**
     * I can't show this automatically, you should look at generated SQL what's the effect:
     * <ul>
     *  <li> If the user->books is {@code inverse="true"}, then collection is the main part that saved the whole
     *    association thus you'll have a single insert of User and then 2 inserts of the book. In these inserts the
     *    association itself will be saved which means that column author_id will carry the value of user.id.</li>
     *  <li> If you try to set {@code inverse="false"} which is the default option, you'll see that first User is
     *    inserted, then book is inserted because there is a cascade, and then the association is inserted as updates
     *    to the book table, Thus you can see that inverse="false" makes Book the main side and its the Book's
     *    responsibility to save the association.</li>
     *  </ul>
     *
     *  <p>That's why in most cases the recommended option is inverse="true".</p>
     *
     *  Don't mix cascades and saving the association - these are different things, cascades would say
     *  <b>"my associated Entity should be saved when I'm saved"</b>
     *  while inverse says
     *  <b>"my <u>association</u> with that entity should be saved when I'm saved"</b>.<br/>
     *
     *  But even though cascades and inversity are different things, cascades usually are set only from inverse side,
     *  we'll talk about this later.
     */
    @Test
    void "bidi-OTM should have a main side"() {
        User user = givenUser()
        user.setBooks(givenTransientBooks(2))
        usersDao.saveOrUpdate(user)
        usersDao.session().flush()
    }

    private static Collection<Book> givenTransientBooks(int amount = 1) {
        Set<Book> books = new HashSet<>(amount)
        for (int i = 0; i < amount; i++) {
            books.add(new Book(title: UUID.randomUUID().toString()))
        }
        return books
    }

    private static User givenUser() {
        AccessCard card = new AccessCard(code: "EK91234", type: AccessCard.Type.USUAL)
        User user = new User(username: "I'm the One", accessCard: card)
        return user
    }

    @Autowired UserDao usersDao;
    @Autowired LibraryDao libraryDao;
}
