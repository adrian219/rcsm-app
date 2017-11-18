package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Adrian Wieczorek on 11/6/2017.
 */
public class AppProperties {
    public static Properties properties = new Properties();
    private static String filePath = "properties/config.properties";

    private static Properties loadProperties() throws IOException {
        InputStream inputStream = Thread.currentThread().getContextClassLoader().getResource(filePath).openStream();
        properties.load(inputStream);

        return properties;
    }

    public static String getProperty(String key) throws IOException {
        Properties properties = loadProperties();

        return properties.getProperty(key);
    }

    public static void setProperty(String key, String value) throws IOException {
        Properties properties = loadProperties();

        properties.setProperty(key, value);
    }
}
