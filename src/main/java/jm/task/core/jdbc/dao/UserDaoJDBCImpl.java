package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String tableName = "new_table";

    public static final String SQL_SELECT_ALL_USERS = "SELECT * FROM " + tableName;

    public static final String SQL_DROP_TABLE = "DROP TABLE `" + tableName + "`";

    public static final String SQL_CREATE_TABLE = "CREATE TABLE `" + tableName + "`" +
            "  (`id` BIGINT(1) NOT NULL AUTO_INCREMENT," +
            "  `name` VARCHAR(45) NOT NULL," +
            "  `last_name` VARCHAR(45) NOT NULL," +
            "  `age` TINYINT(1) NOT NULL," +
            "  PRIMARY KEY (`id`))";

    public static final String SQL_DELETE_BY_ID = "DELETE FROM `" + tableName + "` WHERE id=?";

    public static final String SQL_TRUNCATE = "TRUNCATE `" + tableName + "`";

    public static final String SQL_SAVE_USER = "INSERT INTO `" + tableName +
            "` (name, last_name, age) VALUES (?, ?, ?)";

    public UserDaoJDBCImpl() {
    }

    public void createUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SQL_CREATE_TABLE);
            System.out.println("Table was created");
        } catch (SQLSyntaxErrorException ex) { // такая таблица создана
            System.out.println(ex.getMessage());
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
            exception.printStackTrace();
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SQL_DROP_TABLE);
            System.out.println("Table was dropped");
        } catch (SQLSyntaxErrorException ex) { // такой таблицы не существует
            System.out.println(ex.getMessage());
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
            exception.printStackTrace();
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_SAVE_USER)) {
            statement.setString(1, name);
            statement.setString(2, lastName);
            statement.setByte(3, age);
            statement.executeUpdate();
            System.out.println("User с именем – " + name + " добавлен в базу данных");
        } catch (SQLSyntaxErrorException ex) { // такой таблицы не существует
            System.out.println(ex.getMessage());
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
            exception.printStackTrace();
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            statement.setLong(1, id);
            int isDeleted = statement.executeUpdate();
            System.out.println("User with id: " + id + (isDeleted == 0 ? " was not" : " was") + " removed");
        } catch (SQLSyntaxErrorException ex) { // такой таблицы не существует
            System.out.println(ex.getMessage());
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
            exception.printStackTrace();
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Connection connection = null;
        try {
            connection = Util.getMySQLConnection();
            Statement statement = connection.createStatement();
            connection.setAutoCommit(false);// just try to execute transaction with rollback although it isn`t any sense here
            ResultSet rs = statement.executeQuery(SQL_SELECT_ALL_USERS);
            connection.commit();
            while (rs.next()) {
                long id = rs.getLong(1);
                String name = rs.getString(2);
                String lastName = rs.getString(3);
                Byte age = rs.getByte(4);
                User user = new User(name, lastName, age);
                user.setId(id);
                users.add(user);
            }
        } catch (SQLSyntaxErrorException ex) { // такой таблицы не существует
            System.out.println(ex.getMessage());
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
            exception.printStackTrace();
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        } finally {
            try (Connection conn = connection) {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                System.out.println("The transaction can`t be rollback");
            }
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL_TRUNCATE);
            System.out.println("Table was truncated");
        } catch (SQLSyntaxErrorException ex) { // такой таблицы не существует
            System.out.println(ex.getMessage());
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred.");
            exception.printStackTrace();
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }
}
