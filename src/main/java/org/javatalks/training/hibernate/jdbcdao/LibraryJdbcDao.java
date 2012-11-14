package org.javatalks.training.hibernate.jdbcdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Library;
import org.javatalks.training.hibernate.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;

/** @author stanislav bashkirtsev */
public class LibraryJdbcDao implements Crud<Library> {
    public LibraryJdbcDao(DataSource dataSource, UserJdbcDao userDao) {
        this.dataSource = dataSource;
        this.userDao = userDao;
    }

    @Override
    public void saveOrUpdate(Library entity) throws SQLException {
        if (entity.getId() == null) {
            insert(entity);
        } else {
            update(entity);
        }
    }

    private void insert(Library library) throws SQLException {
        logger.info("Creating library with name [{}]", library.getName());
        cascadeSaveOrUpdateToOwner(library.getOwner());
        //Note that Statement.RETURN_GENERATED_KEYS is DB dependent, e.g. Oracle does not implement it
        //Also HSQLDB can handle this only via an additional query
        //PostgreSQL also doesn't support it, but can still be hacked using RETURNING clause
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, library.getName());
            setOwnerId(statement, library.getOwner());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) { // It is possible if 'SET NOCOUNT ON' in MS SQL which can lead to performance gains
                throw new SQLException("Creating library failed, no rows affected.");
            }
            ResultSet generatedKeys = statement.getGeneratedKeys();//result set will be closed when statement is closed
            if (generatedKeys.next()) {
                library.setId(generatedKeys.getLong(1));
            } else {
                throw new SQLException("Creating library failed, no generated key obtained.");
            }
        }
    }

    private void update(Library library) throws SQLException {
        logger.info("Updating library [{}]", library.getName());
        cascadeSaveOrUpdateToOwner(library.getOwner());
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE)) {
            statement.setString(1, library.getName());
            setOwnerId(statement, library.getOwner());
            statement.setLong(3, library.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating library failed, no rows affected.");
            }
        }
    }

    /**
     * Sets the owner_id for the insert/update statements of the Library. If owner is null, a null will be specified as
     * an argument to the query.
     *
     * @param statement a statement to set its argument#2 to owner_id
     * @param owner     a user that owns the library which should be stored into owner_id
     * @throws SQLException well, who knows
     */
    private void setOwnerId(PreparedStatement statement, User owner) throws SQLException {
        if (owner != null) {
            statement.setLong(2, owner.getId());
        } else {
            statement.setNull(2, Types.BIGINT);
        }
    }


    /**
     * If it was a newly crated User, it will be stored in DB, if not - it will be updated. We have to execute this
     * UPDATE statement because we don't know actually whether the object was changed or not. Alternatively we would
     * need to select properties and compare with what we have now. Third and best option would be to introduce
     * versioning and execute update with {@code ... where version= :current_version}, but for the sake of simplicity
     * let's stop on updating entity every time.
     *
     * @param owner an owner of the library to be stored in DB and generate its ID if it's a brand new object or to be
     *              update if it was changed
     */
    private void cascadeSaveOrUpdateToOwner(User owner) throws SQLException {
        if (owner != null) {
            userDao.saveOrUpdate(owner);
        }
    }

    @Override
    public Library get(long id) throws SQLException {
        Library lib = null;
        try (PreparedStatement statement = getConnection().prepareStatement(SELECT)) {
            statement.setLong(1, id);
            statement.executeQuery();
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                lib = new Library();
                lib.setId(rs.getLong("id"));
                lib.setName(rs.getString("name"));
                //for simplicity we don't want to construct User from joins, so second select for user will be issued
                lib.setOwner(userDao.get(rs.getInt("owner_id")));
            }
        }
        return lib;
    }

    @Override
    public void delete(Library entity) throws SQLException {
        logger.info("Deleting library [{}]", entity.getName());
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE)) {
            statement.setLong(1, entity.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Deleting library failed, no rows affected.");
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
    private final UserJdbcDao userDao;
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String INSERT = "insert into library(name, owner_id) values(?, ?)";
    private static final String UPDATE = "update library set name = ?, owner_id = ? where id = ?";
    private static final String DELETE = "delete from library where id = ?";
    private static final String SELECT = "select * from library where id = ?";
}
