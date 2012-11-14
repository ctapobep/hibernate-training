package org.javatalks.training.hibernate.springdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.entity.Library;
import org.javatalks.training.hibernate.entity.User;
import org.javatalks.training.hibernate.springdao.util.LazySet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/** @author stanislav bashkirtsev */
class RowMappers {
    public static UserMapper userMapper() {
        return new UserMapper();
    }

    public static LibraryMapper libraryMapper(UserSpringJdbcDao userSpringJdbcDao) {
        return new LibraryMapper(userSpringJdbcDao);
    }

    public static BookMapper bookMapper() {
        return new BookMapper();
    }

    public static BookWithAuthorMapper bookWithAuthorMapper() {
        return new BookWithAuthorMapper();
    }

    private static class UserMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            user.setBooksByDao(new LazySet<Book>(user, "books"));
            return user;
        }
    }

    private static class LibraryMapper implements RowMapper<Library> {
        private LibraryMapper(UserSpringJdbcDao userDao) {
            this.userDao = userDao;
        }

        @Override
        public Library mapRow(ResultSet rs, int rowNum) throws SQLException {
            Library lib = new Library();
            lib.setId(rs.getLong("id"));
            lib.setName(rs.getString("name"));
            //for simplicity we don't want to construct User from joins, so second select for user will be issued
            lib.setOwner(userDao.get(rs.getInt("owner_id")));
            return lib;
        }

        private final UserSpringJdbcDao userDao;
    }

    private static class BookMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong(1));
            book.setTitle(rs.getString(2));
            return book;
        }
    }

    private static class BookWithAuthorMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong(1));
            book.setTitle(rs.getString(2));
            User user = new User();
            user.setId(rs.getLong(3));
            user.setUsername(rs.getString(4));
            user.setBooksByDao(new LazySet<Book>(user, "books"));
            book.setAuthor(user);
            return book;
        }
    }
}
