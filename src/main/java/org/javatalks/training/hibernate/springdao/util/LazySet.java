package org.javatalks.training.hibernate.springdao.util;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * This class would notify a user of the API that the collection is not just empty, but actually was not just fetched
 * from DB by DAO and if you need to load the full object, you have to issue a separate SQL statement. See {@link
 * org.javatalks.training.hibernate.entity.User#setBooksByDao(Set)} for example.
 *
 * @author stanislav bashkirtsev
 */
public class LazySet<T> implements Set<T> {
    private final Object owningEntity;
    private final String fieldInOwningEntity;

    public LazySet(Object owningEntity, String fieldInOwningEntity) {
        this.owningEntity = owningEntity;
        this.fieldInOwningEntity = fieldInOwningEntity;
    }

    @Override
    public int size() {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean isEmpty() {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean contains(Object o) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public Iterator<T> iterator() {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public Object[] toArray() {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public <T1 extends Object> T1[] toArray(T1[] a) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean add(T t) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean remove(Object o) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }

    @Override
    public void clear() {
        throw new LazyInitializationException(owningEntity, fieldInOwningEntity);
    }
}
