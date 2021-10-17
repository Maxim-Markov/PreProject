package jm.task.core.jdbc.util;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.util.ResourceBundle;

public class Util {
    private final static String hostName;
    private final static String portName;
    private final static String dbName;
    private final static String userName;
    private final static String password;
    private final static String connectionURL;

    static {
        ResourceBundle resource = ResourceBundle.getBundle("database");
        hostName = resource.getString("db.host");
        portName = resource.getString("db.port");
        dbName = resource.getString("db.name");
        userName = resource.getString("db.user");
        password = resource.getString("db.password");
        connectionURL = "jdbc:mysql://" + hostName + ":" + portName + "/" + dbName;
    }

    public static Connection getMySQLConnection() throws SQLException {
        return DriverManager.getConnection(connectionURL, userName, password);
    }

    public static SessionFactory getHibernateSessionFactory() {
        Configuration configuration = new Configuration();
        Properties settings = new Properties();
        settings.put(Environment.DRIVER, "com.mysql.jdbc.Driver");
        settings.put(Environment.URL, connectionURL);
        settings.put(Environment.USER, userName);
        settings.put(Environment.PASS, password);
        settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL8Dialect");
        settings.put(Environment.SHOW_SQL, "true");
        configuration.setProperties(settings);

        configuration.addAnnotatedClass(User.class);
        StandardServiceRegistryBuilder builder = new StandardServiceRegistryBuilder()
                .applySettings(configuration.getProperties());
        return configuration.buildSessionFactory(builder.build());
    }
}

