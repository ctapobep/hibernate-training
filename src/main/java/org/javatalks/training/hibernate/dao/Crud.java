package org.javatalks.training.hibernate.dao;

/**
 * Base interface for all DAOs for Create-Read-Update-Delete operations.
 *
 * @author stanislav bashkirtsev
 */
public interface Crud<T> {
    Crud<T> save(T entity);

    Crud<T> update(T entity);

    T get(long id);

    Crud<T> delete(T entity);
}
