package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.sql.*;

public class Util {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/taskjdbc?serverTimezone=Europe/Moscow&useSSL=false";
    private static final String DB_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";

    private static SessionFactory sessionFactory;


    public static Connection getConnection() {
        try {
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

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            Configuration config = new Configuration();

            config.addAnnotatedClass(User.class);
            config.setProperty(Environment.DRIVER, DB_DRIVER);
            config.setProperty(Environment.URL, DB_URL);
            config.setProperty(Environment.USER, DB_USER);
            config.setProperty(Environment.PASS, DB_PASSWORD);
            config.setProperty(Environment.HBM2DDL_AUTO, "none");
            config.setProperty(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
            config.setProperty(Environment.CONNECTION_PROVIDER, "org.hibernate.connection.C3P0ConnectionProvider");
            config.setProperty(Environment.SHOW_SQL, "true");

            ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                    .applySettings(config.getProperties()).build();

            sessionFactory = config.buildSessionFactory(serviceRegistry);
        }
        return sessionFactory;
    }

}
