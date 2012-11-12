package org.javatalks.training.hibernate.jdbcdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;

/**
 * This should demonstrate how to work with pure JDBC in order to compare it with Hibernate and other approaches. But we
 * don't actually want to create connections on our own and can use Spring Transaction support to help us, because this
 * is actually what doesn't change regardless whether we're using ORM or JDBC.
 *
 * @author stanislav bashkirtsev
 */
public class UserJdbcDao implements Crud<User> {
    /**
     *
     * @param dataSource we need this in order to get a connection created by Spring, see {@link #getConnection()} if
     *                   you need details
     */
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
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT)) {
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
        //Note that Statement.RETURN_GENERATED_KEYS is DB dependent, Oracle does not implement it
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
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
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE)) {
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
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE)) {
            statement.setLong(1, entity.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting user failed, no rows affected.");
            }
        }
    }

    /**
     * We want to work with Spring's Transaction Support which means that Spring should open connections and start
     * transactions. By the time DAO is reached, connection should be opened and transaction should be started and all
     * this stuff should be bound to the current thread.
     *
     * @return a connection opened by the Spring somewhere on the service layer
     */
    private Connection getConnection() {
        return DataSourceUtils.getConnection(dataSource);
    }

    private final DataSource dataSource;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String INSERT = "insert into users(username) values(?)";
    private static final String UPDATE = "update users set username = ? where id = ?";
    private static final String DELETE = "delete from users where id = ?";
    private static final String SELECT = "select * from users where id = ?";
}
