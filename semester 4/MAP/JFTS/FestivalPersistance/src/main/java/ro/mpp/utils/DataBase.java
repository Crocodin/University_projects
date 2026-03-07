package ro.mpp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DataBase {
    private final Properties properties = Config.getProperties();
    private static final Logger logger = LogManager.getLogger(DataBase.class);

    public Connection getConnection() throws SQLException {
        String url = properties.getProperty("DB_URL");
        String user = properties.getProperty("DB_USER");
        String password = properties.getProperty("DB_PASS");

        logger.info("Connecting to database {}", url);
        return DriverManager.getConnection(url, user, password);
    }
}
