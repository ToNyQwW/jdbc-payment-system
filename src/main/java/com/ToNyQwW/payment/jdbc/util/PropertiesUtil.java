package com.ToNyQwW.payment.jdbc.util;

import com.ToNyQwW.payment.jdbc.exception.PropertyLoadException;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    private PropertiesUtil() {
    }

    private static void loadProperties() {
        var classLoader = PropertiesUtil.class.getClassLoader();

        try (var resourceAsStream = classLoader.getResourceAsStream("application.properties")) {
            if (resourceAsStream == null) {
                throw new PropertyLoadException("application.properties not found");
            }
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            throw new PropertyLoadException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}

