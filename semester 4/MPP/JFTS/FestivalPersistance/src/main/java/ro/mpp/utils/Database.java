package ro.mpp.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    private static Database instance;

    private final Connection connection;

    private static final String USERNAME = Config.getProperties().getProperty("DB_URL");
    private static final String PASSWORD = Config.getProperties().getProperty("DB_PASS");
    private static final String URL = Config.getProperties().getProperty("DB_USER");

    private static final Logger logger = LogManager.getLogger(Database.class);


    private Database() {
        try {
            logger.info("Connecting to database {}", URL);
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw  new RuntimeException("Can't make the connection with the DB",e);
        }
    }

    public static Database getInstance() {
        if (instance == null) {
            logger.info("No instance of Database. Creating a new instance for {}", URL);
            instance = new Database();
        }
        return instance;
    }

    public synchronized Connection getConnection() {
        logger.info("Getting instance to database {}", URL);
        return connection;
    }
}