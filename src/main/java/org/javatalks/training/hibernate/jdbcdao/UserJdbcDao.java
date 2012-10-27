package org.javatalks.training.hibernate.jdbcdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;

/** @author stanislav bashkirtsev */
public class UserJdbcDao implements Crud<User> {
    public UserJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveOrUpdate(User entity) throws SQLException {
        logger.info("Creating user with name [{}]", entity.getUsername());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into users(username) values(?)", Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.getUsername());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();//result set will be closed when statement is closed
            if (generatedKeys.next()) {
                entity.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no generated key obtained.");
            }
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
