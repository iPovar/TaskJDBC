package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoHibernateImpl implements UserDao {

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        execute("CREATE TABLE IF NOT EXISTS users (id integer primary key auto_increment, name varchar(100), lastName varchar(100), age int)");
    }

    @Override
    public void dropUsersTable() {
        execute("DROP TABLE IF EXISTS users");
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        Transaction transaction = null;
        Session session = Util.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            User user = new User(name, lastName, age);
            Long userID = (Long) session.save(user);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction == null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;
        Session session = Util.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            User user = (User) session.get(User.class, id);
            session.delete(user);
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction == null) {
                transaction.rollback();
            }
        } finally {
            session.close();
        }
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction = null;
        Session session = Util.getSessionFactory().openSession();
        List<User> result;
        try {
            transaction = session.beginTransaction();
            result = session.createSQLQuery("SELECT * FROM users").addEntity(User.class).list();
            transaction.commit();
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction == null) {
                transaction.rollback();
            }
            result = new ArrayList<>();
        } finally {
            session.close();
        }
        return result;
    }

    @Override
    public void cleanUsersTable() {
        execute("TRUNCATE TABLE users");
    }

    private boolean execute(String sql) {
        Transaction transaction = null;
        Session session = Util.getSessionFactory().openSession();
        try {
            transaction = session.beginTransaction();
            session.createSQLQuery(sql).executeUpdate();
            transaction.commit();
            return true;
        } catch (HibernateException e) {
            e.printStackTrace();
            if (transaction == null) {
                transaction.rollback();
            }
            return false;
        } finally {
            session.close();
        }
    }
}
