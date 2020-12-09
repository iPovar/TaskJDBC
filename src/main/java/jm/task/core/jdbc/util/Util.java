package jm.task.core.jdbc.util;

import java.sql.*;

public class Util {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/taskjdbc?serverTimezone=Europe/Moscow&useSSL=false";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    public static Connection getConnection() {
        try {
            //Class.forName(DB_DRIVER);
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void closeConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}
