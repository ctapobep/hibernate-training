package org.javatalks.training.hibernate;

/**
 * Base interface for all DAOs for Create-Read-Update-Delete operations.
 *
 * @author stanislav bashkirtsev
 */
public interface Crud<T> {
    void saveOrUpdate(T entity);

    T get(long id);

    void delete(T entity);
}
