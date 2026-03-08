package ro.mpp.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    // path from the resource package
    private static final String CONFIG_PATH = "config.properties";
    private static final Properties PROPERTIES = initializeProperties();

    public static Properties getProperties() {
        return PROPERTIES;
    }

    private static Properties initializeProperties() {
        Properties prop = new Properties();
        try (InputStream in = Config.class.getClassLoader().getResourceAsStream(CONFIG_PATH)) {
            if (in == null) {
                throw new FileNotFoundException(CONFIG_PATH + " not found");
            }
            prop.load(in);
            return prop;
        } catch (IOException e) {
            throw new RuntimeException("Cannot load config properties." ,e);
        }
    }
}