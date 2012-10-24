package org.javatalks.training.hibernate;

import java.sql.SQLException;

/**
 * Base interface for all DAOs for Create-Read-Update-Delete operations.
 *
 * @author stanislav bashkirtsev
 */
public interface Crud<T> {
    void saveOrUpdate(T entity) throws SQLException;

    T get(long id);

    void delete(T entity);
}
