package vn.iotstar.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class AppProperties {
    private static final Properties PROPERTIES = load();

    private AppProperties() {
    }

    public static String get(String key, String defaultValue) {
        String value = PROPERTIES.getProperty(key);
        if (value == null || value.isBlank()) {
            return defaultValue;
        }
        return value.trim();
    }

    public static int getInt(String key, int defaultValue) {
        try {
            return Integer.parseInt(get(key, String.valueOf(defaultValue)));
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    public static boolean getBoolean(String key, boolean defaultValue) {
        return Boolean.parseBoolean(get(key, String.valueOf(defaultValue)));
    }

    private static Properties load() {
        Properties properties = new Properties();
        try (InputStream inputStream = AppProperties.class.getClassLoader().getResourceAsStream("application.properties")) {
            if (inputStream != null) {
                properties.load(inputStream);
            }
        } catch (IOException ignored) {
        }
        return properties;
    }
}
