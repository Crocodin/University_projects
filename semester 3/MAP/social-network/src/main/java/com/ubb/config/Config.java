package com.ubb.config;


import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPERTIES = initProperties();

    public static Properties getProperties() {
        return PROPERTIES;
    }

    private static Properties initProperties() {
        Properties properties = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input == null) {
                throw new RuntimeException("Cannot load config properties");
            }
            properties.load(input);
            return properties;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties", e);
        }
    }
}
