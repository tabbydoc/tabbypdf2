package ru.icc.td.tabbypdf2.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class AppConfig {

    // ANN Model
    private static boolean useANNModel;
    private static String pathToANNModel;
    private static String pathToLabelMap;
    // Debug
    private static boolean saveToIcdar;
    private static boolean SaveToExcel;
    // Postprocessing
    private static boolean usePostProcessing;
    // Extractor
    private static boolean useExtractor;
    private static String extractorMode;

    static {
        Properties properties = new Properties();
        ClassLoader classLoader = AppConfig.class.getClassLoader();
        try {
            InputStream inputStream = classLoader.getResourceAsStream("application.properties");
            properties.load(inputStream);
            // ANN Model
            useANNModel = Boolean.parseBoolean(properties.getProperty("model.use_model"));
            pathToANNModel = properties.getProperty("model.path_to_model");
            pathToLabelMap = properties.getProperty("model.path_to_label_map");
            // Debug
            saveToIcdar = Boolean.parseBoolean(properties.getProperty("debug.save_to_icdar"));
            SaveToExcel = Boolean.parseBoolean(properties.getProperty("debug.save_to_excel"));
            // Postprocessing
            usePostProcessing = Boolean.parseBoolean(properties.getProperty("processing.use"));
            // Extractor
            useExtractor = Boolean.parseBoolean(properties.getProperty("extractor.use"));
            extractorMode = properties.getProperty("extractor.mode");
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

    public static boolean isSaveToExcel() {
        return SaveToExcel;
    }

    public static boolean isUseExtractor() {
        return useExtractor;
    }

    public static String getExtractorMode() {
        return extractorMode;
    }
}
