package ru.icc.td.tabbypdf2.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    public static boolean useANNModel;
    public static String pathToANNModel;
    public static String pathToLabelMap;

    static {
        Properties properties = new Properties();
        ClassLoader classLoader = AppConfig.class.getClassLoader();
        try {
            InputStream inputStream = classLoader.getResourceAsStream("application.properties");
            properties.load(inputStream);
            useANNModel = Boolean.parseBoolean(properties.getProperty("model.use_model"));
            pathToANNModel = properties.getProperty("model.path_to_label_map");
            pathToLabelMap = properties.getProperty("model.path_to_model");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
