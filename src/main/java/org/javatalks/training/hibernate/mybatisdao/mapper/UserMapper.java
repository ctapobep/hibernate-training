package org.javatalks.training.hibernate.mybatisdao.mapper;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.User;

/** @author stanislav bashkirtsev */
public interface UserMapper extends Crud<User> {
    User getWithBooks(long id);
}
