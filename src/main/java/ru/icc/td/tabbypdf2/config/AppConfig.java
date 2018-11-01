package ru.icc.td.tabbypdf2.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static boolean useANNModel;
    private static String pathToANNModel;
    private static String pathToLabelMap;

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

    public static boolean isUseANNModel() {
        return useANNModel;
    }

    public static String getPathToANNModel() {
        return pathToANNModel;
    }

    public static String getPathToLabelMap() {
        return pathToLabelMap;
    }
}
