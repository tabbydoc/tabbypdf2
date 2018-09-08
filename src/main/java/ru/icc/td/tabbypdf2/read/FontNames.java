package ru.icc.td.tabbypdf2.read;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class FontNames {

    private final Set<String> fontNames;

    private final static FontNames instance = new FontNames();

    private FontNames() {
        fontNames = new HashSet();
        loadResource();
    }

    private void loadResource() {
        ClassLoader classLoader = getClass().getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream("font_names")) {
            Scanner scanner = new Scanner(inputStream);
            while(scanner.hasNextLine()){
                fontNames.add(scanner.nextLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Normalize a font name
    public String normFontName(String fontName) {
        Pattern p;
        Matcher m;

        for (String fn : fontNames) {
            p = Pattern.compile(fn, Pattern.CASE_INSENSITIVE);
            m = p.matcher(fontName);
            if (m.find())
                return fn;
        }

        return null;
    }

    public static FontNames getInstance() {
        return FontNames.instance;
    }
}