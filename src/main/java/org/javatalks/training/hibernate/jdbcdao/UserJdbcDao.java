package org.javatalks.training.hibernate.jdbcdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;

/**
 * Note issues that pure JDBC brings with it:
 * <ul>
 *     <li>All exceptions are {@link SQLException}, there is no hierarchy we can rely on to distinguish between problems
 *     that occurred. Also this exception is checked, we don't want this for 100% cases since it makes API ugly.</li>
 *     <li>The code is DB dependant, take a look at {@link #insert(User)}</li>
 *     <li>The code is pretty complicated because we need to work with {@link ResultSet} directly</li>
 * </ul>
 * For other issues of JDBC see {@link LibraryJdbcDao}.
 * @author stanislav bashkirtsev
 */
public class UserJdbcDao implements Crud<User> {
    /**
     * @param dataSource we need this in order to get a connection created by Spring, see {@link #getConnection()} if
     *                   you need details
     */
    public UserJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveOrUpdate(User entity) throws SQLException {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    /**
     * Batch insert results in a less traffic going forth and back. Instead of separate INSERT statements, we have:
     * {@code insert into users(username) values("first"), ("second")}. This might improve performance significantly.
     * Note, though that this might be a DB specific feature and you might need to configure it for your database.
     *
     * @param users users to batchly insert into DB
     * @throws SQLException
     */
    public void batchInsert(Iterable<User> users) throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT)) {
            int userIndex = 0;
            for (User user : users) {
                statement.setString(1, user.getUsername());
                statement.addBatch();
                if (++userIndex % 500 == 0) {//to escape OutOfMemoryException
                    statement.executeBatch();
                }
            }
            if (userIndex % 500 != 0) { //let's dump everything that's left
                statement.executeBatch();
            }
        }
    }

    @Override
    public User get(long id) throws SQLException {
        User user = null;
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                user = new User();
                user.setId(rs.getLong("id"));
                user.setUsername(rs.getString("username"));
            }
        }
        return user;
    }


    private void insert(User user) throws SQLException {
        logger.info("Creating user with name [{}]", user.getUsername());
        //Note that Statement.RETURN_GENERATED_KEYS is DB dependent, e.g. Oracle does not implement it
        //Also HSQLDB can handle this only via an additional query
        //Different versions of PostgreSQL also work differently depending on mechanism of id generation, they can be using RETURNING clause in order to get the ID back
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, user.getUsername());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) { // It is possible if 'SET NOCOUNT ON' in MS SQL which can lead to performance gains
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

    public long count() throws SQLException {
        try (PreparedStatement statement = getConnection().prepareStatement(COUNT)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return resultSet.getLong(1);
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
    private static final String COUNT = "select count(*) from users";
}
