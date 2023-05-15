package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

import static jm.task.core.jdbc.util.Util.getSessionFactory;

public class UserDaoHibernateImpl implements UserDao {

    private Transaction transaction;

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                String sql = "CREATE TABLE IF NOT EXISTS user " +
                        "(id BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
                        "name VARCHAR(50) NOT NULL, lastName VARCHAR(50) NOT NULL, " +
                        "age TINYINT NOT NULL)";
                Query query = session.createSQLQuery(sql).addEntity(User.class);
                query.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void dropUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            String sql = "DROP TABLE IF EXISTS user";
            Query query = session.createSQLQuery(sql).addEntity(User.class);
            query.executeUpdate();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        try (Session session = getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                session.save(new User(name, lastName, age));
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("User с именем - " + name + " добавлен в базу данных");
    }

    @Override
    public void removeUserById(long id) {
        try (Session session = getSessionFactory().openSession()) {
            try {
                transaction = session.beginTransaction();
                User user = session.get(User.class, id);
                session.delete(user);
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<User> getAllUsers() {
        List<User> userList = null;
        try (Session session = getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Query query = session.createQuery("FROM User");
            userList = query.list();
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userList;
    }

    @Override
    public void cleanUsersTable() {
        try (Session session = getSessionFactory().openSession()) {
            try {
                Transaction transaction = session.beginTransaction();
                Query query = session.createQuery("DELETE FROM User");
                query.executeUpdate();
                transaction.commit();
            } catch (Exception e) {
                transaction.rollback();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
