package org.javatalks.training.hibernate.jdbcdao;

import org.javatalks.training.hibernate.Crud;
import org.javatalks.training.hibernate.entity.User;

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
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("insert into USERS(username) values(?)")) {
            statement.setString(1, entity.getUsername());
        }
    }

    @Override
    public User get(long id) throws SQLException {
        User user = null;
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement("select * from USERS where ID=?");
             ResultSet rs = statement.executeQuery()) {
            if(rs.next()){
                user = new User();
                user.setId(rs.getInt("ID"));
                user.setUsername(rs.getString("USERNAME"));
            }
        }
        return user;
    }

    @Override
    public void delete(User entity) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    private final DataSource dataSource;
}
