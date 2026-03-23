package ro.mpp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private Connection connection;

    private static final String URL      = Config.getProperties().getProperty("DB_URL");
    private static final String USERNAME = Config.getProperties().getProperty("DB_USER");
    private static final String PASSWORD = Config.getProperties().getProperty("DB_PASS");

    private static final Logger logger = LogManager.getLogger(Database.class);

    public Database() {
        logger.info("Database instance created (connection is lazy)");
    }

    private Connection openConnection() throws SQLException {
        logger.info("Opening new connection to {}", URL);
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }

    public Connection  getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            logger.info("Connection absent or closed — opening a new one");
            connection = openConnection();
        }
        return connection;
    }

    public void close() {
        if (connection != null) {
            try {
                connection.close();
                logger.info("Connection to {} closed", URL);
            } catch (SQLException e) {
                logger.error("Error closing connection: {}", e.getMessage());
            } finally {
                connection = null;
            }
        }
    }
}