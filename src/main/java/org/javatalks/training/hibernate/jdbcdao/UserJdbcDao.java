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
        if(entity.getId() == null){
            insert(entity);
        } else{
            update(entity);
        }
    }

    @Override
    public User get(long id) throws SQLException {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(SELECT)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            if(rs.next()){
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
            }
        }
        return user;
    }


    private void insert(User user) throws SQLException {
        logger.info("Creating user with name [{}]", user.getUsername());
        try (Connection connection = dataSource.getConnection();
             //Note that Statement.RETURN_GENERATED_KEYS is DB dependent
             PreparedStatement statement = connection.prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Creating user failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();//result set will be closed when statement is closed
            if (generatedKeys.next()) {
                user.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating user failed, no generated key obtained.");
            }
        }
    }

    private void update(User user) throws SQLException {
        logger.info("Updating user [{}]", user.getUsername());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(UPDATE)) {
            statement.setString(1, user.getUsername());
            statement.setLong(2, user.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating user failed, no rows affected.");
            }
        }
    }

    @Override
    public void delete(User entity) throws SQLException {
        logger.info("Deleting user [{}]", entity.getUsername());
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(DELETE)) {
            statement.setLong(1, entity.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
        }
    }

    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String INSERT = "insert into users(username) values(?)";
    private static final String UPDATE = "update users set username = ? where id = ?";
    private static final String DELETE = "delete from users where id = ?";
    private static final String SELECT = "select * from users where id = ?";
}
