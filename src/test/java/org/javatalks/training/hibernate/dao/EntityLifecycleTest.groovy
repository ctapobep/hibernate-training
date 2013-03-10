package org.javatalks.training.hibernate.dao

import org.hibernate.LockOptions
import org.hibernate.ObjectDeletedException
import org.hibernate.ObjectNotFoundException
import org.javatalks.training.hibernate.entity.Book
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.test.context.transaction.TransactionConfiguration
import org.springframework.transaction.annotation.Transactional

import java.lang.reflect.Field

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@TransactionConfiguration
@Transactional
class EntityLifecycleTest {

    @Test
    void "save() sets the ID since generator=native is used"() {
        Book book = new Book(title: "I need an ID to be generated and set!")
        sut.save(book)
        assert book.id != null
    }

    //*******************
    //***SESSION CACHE***
    //*******************
    @Test
    void "get() returns same reference to object that was stored because it's taken from session-level cache"() {
        Book saved = new Book(title: "I need to be stored and retrieved from session level cache!")
        sut.save(saved)
        Book fromCache = sut.get(saved.id)
        assert fromCache.is(saved)
    }

    @Test
    void "load() also returns object from the cache if it's there"() {
        Book saved = new Book(title: "I need to be stored and retrieved from session level cache!")
        sut.save(saved)
        Book fromCache = sut.load(saved.id)
        assert fromCache.is(saved)
    }

    @Test
    void "get() should load the same entity from DB because it was evicted from session cache"() {
        Book saved = new Book(title: "I need to be loaded from DB because I was removed from session level cache!")
        sut.save(saved)
        sut.session().flush()
        sut.session().evict(saved) //now the object is detached!

        Book loadedAgainFromDb = sut.get(saved.id)
        assert !saved.is(loadedAgainFromDb) // not the same reference
        assertReflectionEquals(saved, loadedAgainFromDb) // but the same values
    }

    // **********
    // ***LOAD***
    // **********
    @Test
    void "load() should create a proxy instead of retrieving object from DB"() {
        Book saved = new Book(title: "Soon I'll be a proxy")
        sut.save(saved)
        sut.session().evict(saved)

        Book proxyOfBook = sut.load(saved.id)

        assert proxyOfBook.id == saved.id //where do we get ID? well, we specified it into load() ;)
        assert proxyOfBook instanceof Book //the proxy is a subclass of Book created in runtime
        assert proxyOfBook.class.name == "org.javatalks.training.hibernate.entity.Book_\$\$_javassist_0"

        //demonstrate that the object is not loaded, its all fields are null (we need to bypass getter here)
        Field titleField = Book.class.getDeclaredField("title")
        titleField.accessible = true
        assert titleField.get(proxyOfBook) == null // here we go, nothing is loaded yet
    }

    @Test
    void "load() should issue select when one of getters is invoked"() {
        Book saved = new Book(title: "Soon I'll be a proxy")
        sut.save(saved)
        evict(saved)

        Book proxyOfBook = sut.load(saved.id)

        Field titleField = Book.class.getDeclaredField("title")
        titleField.accessible = true
        //now let's initialize the object, select is going to be issued
        assert proxyOfBook.title == "Soon I'll be a proxy" //yap, that's what was saved, we fetched that from DB
        assert titleField.get(proxyOfBook) == null //hehe, still nothing, because it simply delegates all the job to INTERNAL Book, proxy doesn't contain values per se!
        //now let's actually see where title value is
        assert proxyOfBook.handler.target.title == "Soon I'll be a proxy" //each proxy has a Handler which knows how to load lazy objects
    }

    @Test(expected = ObjectNotFoundException.class)
    void "load() will throw if there is no such record in DB when we initialize the proxy"() {
        Book book = sut.load(-1L)
        book.title
    }

    // ************
    // ***UPDATE***
    // ************
    @Test
    void "update() is not necessary for Hibernate to update the state of changed object"() {
        Book saved = new Book(title: "I'm going to be stored!")
        sut.save(saved) //we need that only to associate object with the session

        saved.title = "I'm going to be stored even though save() was not invoked"
        sut.session().flush() //flushes all outstanding changes to objects

        sut.session().evict(saved)
        assert sut.get(saved.id).title == "I'm going to be stored even though save() was not invoked"
    }

    // ***********
    // ***MERGE***
    // ***********
    @Test
    void "merge() is saving if object was transient"() {
        Book toBeMerged = new Book(title: "I'm going to be stored!")
        Book merged = (Book) sut.merge(toBeMerged)//after that original object should be thrown away!

        assert toBeMerged.id == null
        assert merged.id != null
    }

    @Test
    void "merge() does nothing if object is in session"() {
        Book originalBook = new Book(title: "I'm going to be stored!")
        sut.save(originalBook)

        Book merged = (Book) sut.merge(originalBook) //no-op, but still safer not to reuse the same object, use the returned one!
        assert merged.is(originalBook)
    }

    @Test
    void "merge() is attaching if object was detached"() {
        Book originalBook = new Book(title: "I'm going to be stored!")
        sut.save(originalBook)
        evict(originalBook)

        Book merged = (Book) sut.merge(originalBook) //issues select
        assertReflectionEquals(originalBook, merged)
    }

    @Test
    void "merge() is updating if object was detached and changed"() {
        Book originalBook = new Book(title: "I'm going to be stored!")
        sut.save(originalBook)
        evict(originalBook)

        originalBook.title = "Updated by merge"
        Book merged = (Book) sut.merge(originalBook) //issues select and copies new value to the selected object
        sut.session().flush() //issues update
        assertReflectionEquals(originalBook, merged)
    }

    /**
     * If you really know that object wasn't changed since it was loaded and detached, but you want to attach it to
     * the session without any extra selects, you may want to use lock(). But if the object was actually changed,
     * Hibernate won't know about that and won't issue update query during flush.
     */
    @Test
    void "lock() is a trick that allows attaching of the object without issuing select"() {
        Book originalBook = new Book(title: "First value which is not going to be overwritten")
        sut.save(originalBook)
        evict(originalBook)

        originalBook.title = "Updated by merge"
        sut.session().buildLockRequest(LockOptions.NONE).lock(originalBook) //now object is in the session
        sut.session().flush() //does not issue because Hibernate doesn't know that object was changed
        evict(originalBook)
        assert sut.get(originalBook.id).title == "First value which is not going to be overwritten"
    }

    @Test
    void "delete() should remove record from DB"() {
        assert sut.count() == 0, "Make sure the DB is empty before you're running the tests"
        Book book = new Book(title: "I'm going to be deleted after store!")
        sut.save(book)
        sut.delete(book)
        assert sut.count() == 0
    }

    @Test(expected = ObjectDeletedException.class)
    void "delete() should not be followed by update()"() {
        Book book = new Book(title: "Don't try to update me after you removed me!")
        sut.save(book)
        sut.delete(book)

        sut.update(book)//you can't update the same object that was removed. of course unless you evict the object
    }

    @Test
    void "saveOrUpdate() will insert new record if entity was just removed"() {
        Book book = new Book(title: "I'll be back 8-)")
        sut.save(book)
        long originalId = book.id
        sut.delete(book)

        sut.saveOrUpdate(book)//save() is actually invoked under the hood, which inserts a new record
        assert book.id != originalId
    }

    /**
     * Flushes session and evicts the specified entity from it. Flush is actually very important: if we use sequences,
     * the object might not been inserted yet, evicting it without flushing will result in object being never inserted.
     *
     * @param entity an entity to be removed from first level cache thus becomes detached
     */
    private void evict(Object entity) {
        sut.session().flush()
        sut.session().evict(entity)
    }

    @Autowired
    private BookDao sut;
}
