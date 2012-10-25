package org.javatalks.training.hibernate.jdbcdao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTransactionalTestNGSpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/** @author stanislav bashkirtsev */
@ContextConfiguration(locations = {"classpath:/org/javatalks/training/hibernate/appContext.xml"})
@TransactionConfiguration(transactionManager = "transactionManager", defaultRollback = true)
@Transactional
public class UserJdbcDaoTest extends AbstractTransactionalTestNGSpringContextTests {

    @Test
    public void testSaveOrUpdate() throws Exception {

    }

    @Test
    public void testGet() throws Exception {

    }

    @Test
    public void testDelete() throws Exception {

    }

    @Autowired
    private UserJdbcDao sut;
}
