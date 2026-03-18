package com.saucedemo.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigReader {
    private static final String CONFIG_FILE = "config.properties";
    private static final Properties PROPS = new Properties();

    static {
        try (InputStream is = ConfigReader.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (is == null) {
                throw new IllegalStateException("Could not find " + CONFIG_FILE + " on the classpath.");
            }
            PROPS.load(is);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to load " + CONFIG_FILE, e);
        }
    }

    private ConfigReader() {}

    public static String get(String key) {
        String value = PROPS.getProperty(key);
        if (value == null) {
            throw new IllegalArgumentException("Missing config key: " + key);
        }
        return value.trim();
    }

    public static String getOrDefault(String key, String defaultValue) {
        String value = PROPS.getProperty(key);
        return value == null ? defaultValue : value.trim();
    }

    public static int getIntOrDefault(String key, int defaultValue) {
        String value = PROPS.getProperty(key);
        if (value == null) return defaultValue;
        return Integer.parseInt(value.trim());
    }
}
