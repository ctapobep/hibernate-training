package org.javatalks.training.hibernate.springdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.springdao.util.WrongCascadeDirectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** @author stanislav bashkirtsev */
public class BookSpringJdbcDao implements Crud<Book> {
    public BookSpringJdbcDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcInsert = new SimpleJdbcInsert(jdbcTemplate);
        jdbcInsert.setTableName("book");
        jdbcInsert.setGeneratedKeyName("id");
    }

    @Override
    public void saveOrUpdate(Book entity) {
        throwIfConstraintsViolated(entity);
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    private void insert(Book entity) {
        logger.info("Creating book with title [{}]", entity.getTitle());
        Map<String, Object> valuesToInsert = new HashMap<>();
        valuesToInsert.put("title", entity.getTitle());
        valuesToInsert.put("author_id", entity.getAuthorId());
        Number bookId = jdbcInsert.executeAndReturnKey(valuesToInsert);
        entity.setId((Long) bookId);
    }

    private void update(Book entity) {
        logger.info("Updating book [{}]", entity.getTitle());
        int affectedRows = jdbcTemplate.update(UPDATE, entity.getTitle(), entity.getAuthorId(), entity.getId());
        if (affectedRows == 0) {
            throw new DataIntegrityViolationException("Updating book failed, no rows affected.");
        }
    }

    private void throwIfConstraintsViolated(Book entity) {
        if (entity.getAuthor() == null) {
            throw new DataIntegrityViolationException("Violating not-null constraint. Author should've been set for Book [" + entity.getId() + "]");
        } else if (entity.getAuthorId() == null) {
            throw new WrongCascadeDirectionException(entity, entity.getAuthor());
        }
    }

    @Override
    public Book get(long id) {
        List<Book> books = jdbcTemplate.query(SELECT, new Object[]{id}, RowMappers.bookRowMapper());
        return books.isEmpty() ? null : books.get(0);
    }

    @Override
    public void delete(Book entity) {
        logger.info("Deleting book [{}]", entity.getTitle());
        int affectedRows = jdbcTemplate.update(DELETE, entity.getId());
        if (affectedRows == 0) {
            throw new DataIntegrityViolationException("Deleting book failed, no rows affected.");
        }
    }

    /**
     * Well, here we go with crappy methods that don't ever finish. This method looks through the result set and
     * constructs the Author of the book from it. Note, that the Author will contain only the book that was returned by
     * this method so that we don't select the whole database. <p/> It's not cool to work with such methods, but this
     * shows that without using a real ORM tool, we can't do real OOP. This makes us to use {@code Long authorId}
     * instead of {@code User author}.
     *
     * @param id the book id to be fetched
     * @return the book with Author being initialized, note that only primitive properties are initialized
     */
    public Book getWithAuthor(long id) {
        List<Book> books = jdbcTemplate.query(SELECT_WITH_AUTHOR, new Object[]{id}, RowMappers.bookRowMapper());
        return books.isEmpty() ? null : books.get(0);
    }

    private final JdbcTemplate jdbcTemplate;
    private final SimpleJdbcInsert jdbcInsert;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String UPDATE = "update book set title = ?, author_id = ? where id = ?";
    private static final String DELETE = "delete from book where id = ?";
    private static final String SELECT = "select * from book as book where book.id = ?";
    /**
     * Eh, JDBC doesn't return columns with prefixes if we simply use {@code select *} which means author.id and book.id
     * would be returned as id and id. So we have to specified those prefixes manually and thus to iterate through all
     * the columns.
     */
    private static final String SELECT_WITH_AUTHOR =
            "select book.id `book.id`, book.title `book.title`, " +
                    "author.id `author.id`, author.username `author.username` " +
                    "from book as book left join users as author on book.author_id = author.id where book.id = ?";
}
