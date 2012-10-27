package org.javatalks.training.hibernate.jdbcdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/** @author stanislav bashkirtsev */
public class UserJdbcDao implements Crud<User> {
    public UserJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveOrUpdate(User entity) throws SQLException {
        logger.info("Creating user with name [{}]", entity.getUsername());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into users(username) values(?)")) {
            statement.setString(1, entity.getUsername());
            statement.executeUpdate();
        }
    }

    @Override
    public User get(long id) throws SQLException {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from users where id=?");
             ResultSet rs = statement.executeQuery()) {
            if(rs.next()){
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
            }
        }
        return user;
    }

    @Override
    public void delete(User entity) throws SQLException {
        logger.info("Deleting user [{}]", entity.getUsername());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("delete from users where id=?")) {
            statement.setString(1, entity.getUsername());
        }
    }

    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(getClass());
}
