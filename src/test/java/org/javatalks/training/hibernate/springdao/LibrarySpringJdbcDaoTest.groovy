package org.javatalks.training.hibernate.springdao

import org.javatalks.training.hibernate.entity.Library
import org.javatalks.training.hibernate.entity.User
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
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
class LibrarySpringJdbcDaoTest {
    @Test
    void saveOrUpdateShouldInsert() {
        Library lib = new Library(name: "The Empty Library of Extincted (ELE)")
        sut.saveOrUpdate(lib)
        assertReflectionEquals(lib, sut.get(lib.id))
    }

    @Test
    void "saveOrUpdate() should insert. With Owner"() {
        User owner = new User(username: "Mr. Nobody")
        Library lib = new Library(name: "The National Ukrainian Library (NUL)", owner: owner)
        sut.saveOrUpdate(lib)

        Library actual = sut.get(lib.id)
        assertAllFieldsEqual(actual, lib)
    }

    @Test
    void "get() should return full object"() {
        Library expected = givenSavedLibraryWithOwner()
        Library actual = sut.get(expected.id)
        assertAllFieldsEqual(actual, expected)
    }

    @Test
    void getShouldReturnFullObject_WithoutOwner() {
        Library expected = givenSavedLibrary()
        Library actual = sut.get(expected.id)
        assertReflectionEquals(expected, actual)
    }

    @Test
    void deleteShouldNotLeaveRecordsInDb() {
        Library lib = givenSavedLibrary()
        sut.delete(lib)
        assert sut.get(lib.id) == null
    }

    @Test
    void updateShouldChangeAllColumns() throws Exception {
        Library alreadySaved = givenSavedLibrary()
        alreadySaved.name = "The Comics Library for Retarded Children (CLRC)"
        sut.saveOrUpdate(alreadySaved)
        assertReflectionEquals(alreadySaved, sut.get(alreadySaved.id));
    }

    @Test(expected = DataIntegrityViolationException.class)
    public void updateShouldThrowIfIdIsWrong() throws Exception {
        Library alreadySaved = givenSavedLibrary()
        alreadySaved.name = "The Comics Library for Retarded Children (CLRC)"
        alreadySaved.id = -1L
        sut.saveOrUpdate(alreadySaved)
    }

    Library givenSavedLibrary() {
        Library lib = new Library(name: "The National Ukrainian Library (NUL)")
        sut.saveOrUpdate(lib)
        return lib
    }


    Library givenSavedLibraryWithOwner() {
        User owner = new User(username: "Ashurbanipal")
        Library lib = new Library(name: "The Plain Old Assyrian Library (POAL)", owner: owner)
        sut.saveOrUpdate(lib)
        return lib
    }

    private void assertAllFieldsEqual(Library actual, Library expected) {
        actual.owner.setBooksByDao(expected.owner.books)//we shouldn't care about User#books because it's lazy
        assertReflectionEquals(expected, actual)
    }

    @Autowired
    private LibrarySpringJdbcDao sut;
}
