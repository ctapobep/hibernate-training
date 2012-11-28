package org.javatalks.training.hibernate.dao

import org.javatalks.training.hibernate.entity.map.Equipment
import org.javatalks.training.hibernate.entity.map.Table
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
class MapTest {
    @Test
    void "todo"() {
        Equipment equipment = new Equipment()
        equipment.tables.put("t1", new Table(number: "table1"))
        dao.save(equipment).flushAndClearSession()
    }

    @Autowired EquipmentDao dao;
}
