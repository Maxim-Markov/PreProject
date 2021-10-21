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
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            statement.execute(SQL_DROP_TABLE);
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
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
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (Connection connection = Util.getMySQLConnection();
             PreparedStatement statement = connection.prepareStatement(SQL_DELETE_BY_ID)) {
            statement.setLong(1, id);
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try(Connection connection = Util.getMySQLConnection();
            Statement statement = connection.createStatement()){
            ResultSet rs = statement.executeQuery(SQL_SELECT_ALL_USERS);
            while (rs.next()) {
                User user = new User(rs.getString(2), rs.getString(3), rs.getByte(4));
                user.setId(rs.getLong(1));
                users.add(user);
            }
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred");
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
        return users;
    }

    public void cleanUsersTable() {
        try (Connection connection = Util.getMySQLConnection();
             Statement statement = connection.createStatement()) {
            statement.executeUpdate(SQL_TRUNCATE);
        } catch (SQLException exception) {
            System.out.println("Some problems with connection occurred.");
        } catch (Exception throwable) { // прочие исключения
            throwable.printStackTrace();
        }
    }
}
