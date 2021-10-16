package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Util {

    public static Connection getMySQLConnection() throws SQLException {
        ResourceBundle resource = ResourceBundle.getBundle("database");
        String hostName = resource.getString("db.host");
        String portName = resource.getString("db.port");
        String dbName = resource.getString("db.name");
        String userName = resource.getString("db.user");
        String password = resource.getString("db.password");

        return getMySQLConnection(hostName, dbName, portName, userName, password);
    }

    public static Connection getMySQLConnection(String hostName, String dbName, String port,
                                                String userName, String password) throws SQLException {

        String connectionURL = "jdbc:mysql://" + hostName + ":" + port + "/" + dbName;
        return DriverManager.getConnection(connectionURL, userName, password);
    }
}

