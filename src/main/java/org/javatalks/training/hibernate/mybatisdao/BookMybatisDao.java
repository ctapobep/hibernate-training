package org.javatalks.training.hibernate.mybatisdao;

import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.entity.User;
import org.javatalks.training.hibernate.mybatisdao.mapper.BookMapper;

import java.util.Collection;
import java.util.List;

/** @author stanislav bashkirtsev */
public class BookMybatisDao implements BookMapper {
    public BookMybatisDao(BookMapper bookMapper) {
        this.bookMapper = bookMapper;
    }

    @Override
    public List<Book> ofAuthor(User user) {
        return null;
    }

    public void insertOrUpdateBooks(Collection<Book> books) {
        for (Book book : books) {
            insertOrUpdate(book);
        }
    }

    public void insertOrUpdate(Book entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    @Override
    public Book getWithAuthor(long id) {
        return bookMapper.getWithAuthor(id);
    }

    @Override
    public void insert(Book entity) {
        bookMapper.insert(entity);
    }

    @Override
    public void update(Book entity) {
        bookMapper.update(entity);
    }

    @Override
    public Book get(long id) {
        return bookMapper.get(id);
    }

    @Override
    public void delete(Book entity) {
        bookMapper.delete(entity);
    }

    private final BookMapper bookMapper;
}
