package org.javatalks.training.hibernate.jdbcdao

import org.javatalks.training.hibernate.entity.Library
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
class LibraryJdbcDaoTest {
    @Test
    void saveOrUpdateShouldInsert() {
        Library lib = new Library(name: "The National Ukrainian Library (NUL)")
        sut.saveOrUpdate(lib)
        assertReflectionEquals(lib, sut.get(lib.id))
    }

    @Test
    void getShouldReturnFullObject() {
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

    @Test(expected = SQLException.class)
    public void updateShouldThrowIfIdIsWrong() throws Exception {
        Library alreadySaved = givenSavedLibrary()
        alreadySaved.name = "The Comics Library for Retarded Children (CLRC)"
        alreadySaved.id = -1L
        sut.saveOrUpdate(alreadySaved)
        assertReflectionEquals(alreadySaved, sut.get(alreadySaved.id));
    }


    Library givenSavedLibrary() {
        Library lib = new Library(name: "The National Ukrainian Library (NUL)")
        sut.saveOrUpdate(lib)
        return lib
    }

    @Autowired
    private LibraryJdbcDao sut;
}
