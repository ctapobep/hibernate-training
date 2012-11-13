package org.javatalks.training.hibernate.springdao.util;

import org.springframework.dao.DataAccessException;

/**
 * Is thrown when entities had bidirectional association and 'secondary' entity was going to be saved while 'main' was
 * transient. For instance Author has a reference to Book and Book has reference to Author. If Author is the main side
 * of association, then it has to be saved before Book. If this wasn't satisfied and Book was going to be saved before
 * Author, then this exception is raised.
 *
 * @author stanislav bashkirtsev
 */
public class WrongCascadeDirectionException extends DataAccessException {

    /**
     * @param hadToBeChild  the secondary association that had to be saved after main one
     * @param hadToBeParent the main side of association that had to be saved before child was going to be saved
     */
    public WrongCascadeDirectionException(Object hadToBeChild, Object hadToBeParent) {
        super("Entity " + hadToBeParent.getClass().getSimpleName() +
                " had to be saved before " + hadToBeChild.getClass().getSimpleName());
    }
}
