package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.map.Equipment
import org.javatalks.training.hibernate.entity.map.Shelf
import org.javatalks.training.hibernate.entity.map.Stand
import org.javatalks.training.hibernate.entity.map.Table
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner
import org.springframework.transaction.annotation.Transactional

import static org.unitils.reflectionassert.ReflectionAssert.assertReflectionEquals

/**
 * @author stanislav bashkirtsev
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/org/javatalks/training/hibernate/appContext.xml")
@Transactional
class MapTest {
    @Test
    void "mapping primitive-to-entity (effectively OTM)"() {
        Table table = new Table(number: "t1")
        Equipment equipment = new Equipment(libraryName: "lib-name")
        equipment.tables.put("t1", table)
        dao.save(equipment).flushAndClearSession()
        //checking association was saved
        Equipment fromDb = dao.get(equipment.id)
        assertReflectionEquals(table, fromDb.tables["t1"])
        //removing association
        fromDb.tables.remove("t1")
        dao.flushAndClearSession()
        //checking associated element was removed
        assert dao.get(equipment.id).tables.isEmpty()
    }

    @Test
    void "mapping entity as a key"() {
        Shelf shelf = new Shelf(number: "shelf2")
        Stand stand = new Stand(number: "stand2")
        Equipment equipment = new Equipment(libraryName: "lib2")
        equipment.shelves.put(shelf, stand)
        dao.session().save(shelf)
        dao.save(equipment).flushAndClearSession()

        Equipment fromDb = dao.get(equipment.id)
        assertReflectionEquals(stand, fromDb.shelves[shelf])
    }

    @Autowired EquipmentDao dao;
}
