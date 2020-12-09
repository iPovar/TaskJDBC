package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao, AutoCloseable {

    private Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        execute("CREATE TABLE IF NOT EXISTS users (id integer primary key auto_increment, name varchar(100), lastName varchar(100), age int);");
    }

    public void dropUsersTable() {
        execute("DROP TABLE IF EXISTS users;");
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO users (name, lastname, age) VALUES (?, ?, ?);")) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setInt(3, age);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM users WHERE id = ?;")) {
           preparedStatement.setLong(1, id);
            preparedStatement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> userList = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM users;")) {
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                userList.add(new User(resultSet.getString("name"), resultSet.getString("lastName"), (byte) resultSet.getInt("age")));
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
        try (Statement statement = connection.createStatement()) {
            return statement.execute(sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public void close() throws Exception {
        Util.closeConnection(connection);
    }
}
