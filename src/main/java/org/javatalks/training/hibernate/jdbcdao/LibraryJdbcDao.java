package org.javatalks.training.hibernate.jdbcdao;

import org.hibernate.event.spi.SaveOrUpdateEvent;
import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.Library;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author stanislav bashkirtsev
 */
public class LibraryJdbcDao implements Crud<Library> {
    public LibraryJdbcDao(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public void saveOrUpdate(Library entity) throws SQLException {
        try(Connection connection = dataSource.getConnection()){
            connection.prepareStatement("update LIBRARY set NAME := name, ")
        }

    }

    @Override
    public Library get(long id) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void delete(Library entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }
    private final DataSource dataSource;
}
