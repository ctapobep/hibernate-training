package org.javatalks.training.hibernate.springdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.entity.User;
import org.javatalks.training.hibernate.springdao.util.LazySet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.*;

/** @author stanislav bashkirtsev */
public class UserSpringJdbcDao implements Crud<User> {

    public UserSpringJdbcDao(JdbcTemplate jdbcTemplate, BookSpringJdbcDao bookDao) {
        this.jdbcTemplate = jdbcTemplate;
        this.bookDao = bookDao;
        jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.setTableName("users");
        jdbcInsert.setGeneratedKeyName("id");
    }

    @Override
    public void saveOrUpdate(User entity) {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
        cascadeSaveOrUpdateToBooks(entity.getBooks());
    }

    private void update(User entity) {
        logger.info("Updating user [{}]", entity.getUsername());
        int affectedRows = jdbcTemplate.update(UPDATE, entity.getUsername(), entity.getId());
        if (affectedRows == 0) {
            throw new DataIntegrityViolationException("Updating user failed, no rows affected.");
        }
    }

    private void insert(User entity) {
        logger.info("Creating user with name [{}]", entity.getUsername());
        Map<String, Object> valuesToInsert = new HashMap<>();
        valuesToInsert.put("username", entity.getUsername());
        Number userId = jdbcInsert.executeAndReturnKey(valuesToInsert);
        entity.setId((Long) userId);
    }

    @Override
    public User get(long id) {
        List<User> users = jdbcTemplate.query(SELECT, new Object[]{id}, RowMappers.userMapper());
        return users.isEmpty() ? null : users.get(0);
    }

    public User getWithBooks(long id) {
        List<User> users = jdbcTemplate.query(SELECT_WITH_BOOKS, new Object[]{id}, RowMappers.userMapper());
        if (users.isEmpty()) {
            return null;
        }
        User user = users.get(0);
        Set<Book> books = new HashSet<>(bookDao.ofAuthor(user));
        return user.setBooksByDao(books);
    }

    private void cascadeSaveOrUpdateToBooks(Set<Book> books) {
        if (books == null || books instanceof LazySet) {
            return;
        }
        for (Book book : books) {
            bookDao.saveOrUpdate(book);
        }
    }

    @Override
    public void delete(User entity) {
        logger.info("Deleting user [{}]", entity.getUsername());
        int affectedRows = jdbcTemplate.update(DELETE, entity.getId());
        if (affectedRows == 0) {
            throw new DataIntegrityViolationException("Deleting user failed, no rows affected.");
        }
    }

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private final BookSpringJdbcDao bookDao;
    private static final String UPDATE = "update users set username = ? where id = ?";
    private static final String DELETE = "delete from users where id = ?";
    private static final String SELECT = "select * from users where id = ?";
    private static final String SELECT_WITH_BOOKS = "select * from users where id = ?";
}
