package ru.icc.td.tabbypdf2.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    private static boolean useANNModel;
    private static String pathToANNModel;
    private static String pathToLabelMap;
    private static boolean saveToIcdar;
    private static boolean usePostProcessing;

    static {
        Properties properties = new Properties();
        ClassLoader classLoader = AppConfig.class.getClassLoader();
        try {
            InputStream inputStream = classLoader.getResourceAsStream("application.properties");
            properties.load(inputStream);
            useANNModel = Boolean.parseBoolean(properties.getProperty("model.use_model"));
            pathToANNModel = properties.getProperty("model.path_to_model");
            pathToLabelMap = properties.getProperty("model.path_to_label_map");
            saveToIcdar = Boolean.parseBoolean(properties.getProperty("debug.save_to_icdar"));
            usePostProcessing = Boolean.parseBoolean(properties.getProperty("use_postprocessing"));
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

    public static boolean isUsePostProcessing() {
        return usePostProcessing;
    }

    public static boolean isSaveToIcdar() {
        return saveToIcdar;
    }
}
