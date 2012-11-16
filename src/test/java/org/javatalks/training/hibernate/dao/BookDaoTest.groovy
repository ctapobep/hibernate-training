package org.javatalks.training.hibernate.dao

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
class BookDaoTest {
    @Test
    void "save() sets the ID since generator=native is used"() {
        Book book = new Book(title: "I need an ID to be generated and set!")
        sut.insert(book)
        assert book.id != null
    }

    @Test
    void "get() returns same reference to object that was stored because it's taken from session-level cache"() {
        Book saved = new Book(title: "I need to be stored and retrieved from session level cache!")
        sut.insert(saved)
        Book fromCache = sut.get(saved.id)
        assert fromCache.is(saved)
    }

    @Test
    void "load() also returns object from the cache if it's there"() {
        Book saved = new Book(title: "I need to be stored and retrieved from session level cache!")
        sut.insert(saved)
        Book fromCache = sut.load(saved.id)
        assert fromCache.is(saved)
    }

    @Test
    void "get() should load the same entity from DB because it was evicted from session cache"() {
        Book saved = new Book(title: "I need to be loaded from DB because I was removed from session level cache!")
        sut.insert(saved)
        sut.session().evict(saved) //now the object is detached!

        Book loadedAgainFromDb = sut.get(saved.id)
        assert !saved.is(loadedAgainFromDb) // not the same reference
        assertReflectionEquals(saved, loadedAgainFromDb) // but the same values
    }

    @Test
    void "load() should create a proxy instead of retrieving object from DB"() {
        Book saved = new Book(title: "Soon I'll be a proxy")
        sut.insert(saved)
        sut.session().evict(saved)

        Book proxyOfBook = sut.load(saved.id)

        assert proxyOfBook.id == saved.id //where do we get ID? well, we specified it into load() ;)
        assert proxyOfBook instanceof Book //the proxy is a subclass of Book created in runtime
        assert proxyOfBook.class.name == "org.javatalks.training.hibernate.entity.Book_\$\$_javassist_0"

        //demonstrate that the object is not loaded, its all fields are null (we need to bypass getter here)
        Field titleField = Book.class.getDeclaredField("title")
        titleField.accessible = true
        titleField.get(proxyOfBook) == null // here we go, nothing is loaded yet
    }

    @Test
    void "load() should issue select when one of getters is invoked"() {
        Book saved = new Book(title: "Soon I'll be a proxy")
        sut.insert(saved)
        sut.session().evict(saved)

        Book proxyOfBook = sut.load(saved.id)

        Field titleField = Book.class.getDeclaredField("title")
        titleField.accessible = true
        //now let's initialize the object, select is going to be issued
        assert proxyOfBook.title == "Soon I'll be a proxy" //yap, that's what was saved, we fetched that from DB
        titleField.get(proxyOfBook) == null //hehe, still nothing, because it simply delegates all the job to INTERNAL Book, proxy doesn't contain values per se!
        //now let's actually see where title value is
        assert proxyOfBook.@handler.target.title == "Soon I'll be a proxy" //each proxy has a Handler which knows how to load lazy objects
    }


    @Test
    void "update() is not necessary for Hibernate to update the state of changed object"() {
        Book saved = new Book(title: "I'm going to be stored!")
        sut.insert(saved)//we need that only to associate object with the session

        saved.title = "I'm going to be stored even though save() was not invoked"
        sut.session().flush() //flushes all outstanding changes to objects

        sut.session().evict(saved)
        assert sut.get(saved.id).title == "I'm going to be stored even though save() was not invoked"
    }

    @Test
    void "merge() is saving if object was transient"(){
        Book toBeMerged = new Book(title: "I'm going to be stored!")
        Book merged = (Book) sut.merge(toBeMerged)//after that original object should be thrown away!

        assert merged.id != null
        assert toBeMerged.id == null
    }

    @Test
    void "merge() does nothing if object is in session"(){
        Book originalBook = new Book(title: "I'm going to be stored!")
        sut.insert(originalBook)

        Book merged = (Book) sut.merge(originalBook) //no-op, but still safer not to reuse the same object, use the returned one!
        assert merged.is(originalBook)
    }

    @Test
    void "merge() is attaching if object was detached"(){
        Book originalBook = new Book(title: "I'm going to be stored!")
        sut.insert(originalBook)
        sut.session().evict(originalBook)

        Book merged = (Book) sut.merge(originalBook) //issues select
        assertReflectionEquals(originalBook, merged)
    }

    @Test
    void "merge() is updating if object was detached and changed"(){
        Book originalBook = new Book(title: "I'm going to be stored!")
        sut.insert(originalBook)
        sut.session().evict(originalBook)

        originalBook.title = "Updated by merge"
        Book merged = (Book) sut.merge(originalBook) //issues select and copies new value to the selected object
        sut.session().flush() //issues update
        assertReflectionEquals(originalBook, merged)
    }

    @Test
    void "delete() should remove record from DB"() {
        assert sut.count() == 0, "Make sure the DB is empty before you're running the tests"
        Book originalBook = new Book(title: "I'm going to be stored!")
        sut.insert(originalBook)
        sut.delete(originalBook)
        assert sut.count() == 0
    }

    @Autowired
    private BookDao sut;
}
