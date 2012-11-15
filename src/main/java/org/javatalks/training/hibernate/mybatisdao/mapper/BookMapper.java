package org.javatalks.training.hibernate.mybatisdao.mapper;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.entity.User;

import java.util.Collection;
import java.util.List;

/** @author stanislav bashkirtsev */
public interface BookMapper extends Crud<Book> {
    List<Book> ofAuthor(User user);
    Book getWithAuthor(long id);
}
