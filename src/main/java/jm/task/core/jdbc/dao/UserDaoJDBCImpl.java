package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        execute("CREATE TABLE IF NOT EXISTS users (id integer primary key auto_increment, name varchar(100), lastName varchar(100), age tinyint);");
    }

    public void dropUsersTable() {
        execute("DROP TABLE IF EXISTS users;");
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getConnection()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name, lastname, age) VALUES (?, ?, ?);");
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.executeUpdate();
            preparedStatement.close();

            preparedStatement = connection.prepareStatement("SELECT LAST_INSERT_ID() AS ID;");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                User user = new User(name, lastName, age);
                user.setId(resultSet.getLong(1));
            }
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?;")) {
            preparedStatement.setLong(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (Connection connection = Util.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users;")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                User userBuf = new User();
                userBuf.setId(resultSet.getLong("id"));
                userBuf.setName(resultSet.getString("name"));
                userBuf.setLastName(resultSet.getString("lastName"));
                userBuf.setAge(resultSet.getByte("age"));
                userList.add(userBuf);
            }
            return userList;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    public void cleanUsersTable() {
        execute("TRUNCATE TABLE users;");
    }

    private boolean execute(String sql) {
        try (Connection connection = Util.getConnection();
            Statement statement = connection.createStatement()) {
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
