package org.javatalks.training.hibernate.springdao.util;

import org.springframework.dao.DataAccessException;

/**
 * Our own implementation of the Hibernate's exception to demonstrate how we can work with Spring JDBC.
 *
 * @author stanislav bashkirtsev
 * @see LazySet
 * @see org.javatalks.training.hibernate.entity.User
 */
public class LazyInitializationException extends DataAccessException {

    public LazyInitializationException(Object entity, String field) {
        super(entity.getClass() + " contained lazy field: " + field + " which was not loaded during the initial select " +
                "statement and had to be loaded during additional select");
    }
}
