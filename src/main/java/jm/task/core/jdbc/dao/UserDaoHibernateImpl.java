package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;

import javax.persistence.Entity;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class UserDaoHibernateImpl implements UserDao {
    private final static String tableName = User.class.getAnnotation(Entity.class).name();

    public UserDaoHibernateImpl() {

    }

    @Override
    public void createUsersTable() {
        doInTransaction(session -> {
            String sql = "CREATE TABLE `" + tableName + "`" +
                    "  (`id` BIGINT(1) NOT NULL AUTO_INCREMENT," +
                    "  `name` VARCHAR(45) NOT NULL," +
                    "  `last_name` VARCHAR(45) NOT NULL," +
                    "  `age` TINYINT(1) NOT NULL," +
                    "  PRIMARY KEY (`id`))";
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
        });
    }

    @Override
    public void dropUsersTable() {
        doInTransaction(session -> {
            String sql = "DROP TABLE IF EXISTS " + tableName;
            session.createSQLQuery(sql).addEntity(User.class).executeUpdate();
        });
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {
        if (doInTransaction(session ->
                session.saveOrUpdate(new User(name, lastName, age)))) {
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        }
    }

    @Override
    public void removeUserById(long id) {
        doInTransaction(session -> {
            User user = session.get(User.class, id);
            session.delete(user);
        });
    }

    @Override
    public List<User> getAllUsers() {
        Transaction transaction;
        try (Session session = Util.getHibernateSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            CriteriaBuilder builder = session.getCriteriaBuilder();
            CriteriaQuery<User> criteria = builder.createQuery(User.class);
            criteria.from(User.class);
            List<User> list = session.createQuery(criteria).list();
            transaction.commit();
            return list;
        } catch (Exception ex) {
            System.out.println("Some problems occurred. Check if table exists.");
            System.out.println(ex.getMessage());
            return Collections.emptyList();
        }
    }

    @Override
    public void cleanUsersTable() {

        doInTransaction(session ->
                session.createQuery("delete from " + tableName)
                        .executeUpdate());
    }

    private boolean doInTransaction(Consumer<Session> consumer) {
        Transaction transaction;
        try (Session session = Util.getHibernateSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            consumer.accept(session);
            transaction.commit();
            return true;
        } catch (Exception ex) {
            System.out.println("Some problems occurred. Check if table exists.");
            ex.printStackTrace();
            return false;
        }
    }

}
