package org.javatalks.training.hibernate.dao;

import org.hibernate.SessionFactory;
import org.javatalks.training.hibernate.entity.bag.Book;
import org.javatalks.training.hibernate.entity.map.Equipment;

/** @author stanislav bashkirtsev */
public class EquipmentDao extends CrudDao<Equipment> {
    public EquipmentDao(SessionFactory sessionFactory) {
        super(sessionFactory, Equipment.class);
    }
}
