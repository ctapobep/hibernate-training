package org.javatalks.training.hibernate.jdbcdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Library;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.*;

/** @author stanislav bashkirtsev */
public class LibraryJdbcDao implements Crud<Library> {
    public LibraryJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
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
        //Note that Statement.RETURN_GENERATED_KEYS is DB dependent, Oracle does not implement it
        try (PreparedStatement statement = getConnection().prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, library.getName());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
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
        try (PreparedStatement statement = getConnection().prepareStatement(UPDATE)) {
            statement.setString(1, library.getName());
            statement.setLong(2, library.getId());
            int affectedRows = statement.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating library failed, no rows affected.");
            }
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
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final String INSERT = "insert into library(name) values(?)";
    private static final String UPDATE = "update library set name = ? where id = ?";
    private static final String DELETE = "delete from library where id = ?";
    private static final String SELECT = "select * from library where id = ?";
}
