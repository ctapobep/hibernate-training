package org.javatalks.training.hibernate.springdao;

import org.javatalks.training.hibernate.entity.Book;
import org.javatalks.training.hibernate.entity.Library;
import org.javatalks.training.hibernate.entity.User;
import org.javatalks.training.hibernate.springdao.util.LazySet;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/** @author stanislav bashkirtsev */
class RowMappers {
    public static UserRowMapper userRowMapper() {
        return new UserRowMapper();
    }

    public static LibraryRowMapper libraryRowMapper(UserSpringJdbcDao userSpringJdbcDao) {
        return new LibraryRowMapper(userSpringJdbcDao);
    }

    public static BookRowMapper bookRowMapper() {
        return new BookRowMapper();
    }

    public static BookWithAuthorRowMapper bookWithAuthorRowMapper() {
        return new BookWithAuthorRowMapper();
    }

    private static class UserRowMapper implements RowMapper<User> {
        @Override
        public User mapRow(ResultSet rs, int rowNum) throws SQLException {
            User user = new User();
            user.setId(rs.getLong("id"));
            user.setUsername(rs.getString("username"));
            return user;
        }
    }

    private static class LibraryRowMapper implements RowMapper<Library> {
        private LibraryRowMapper(UserSpringJdbcDao userDao) {
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

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Book book = new Book();
            book.setId(rs.getLong(1));
            book.setTitle(rs.getString(2));
            return book;
        }
    }

    private static class BookWithAuthorRowMapper implements RowMapper<Book> {
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
