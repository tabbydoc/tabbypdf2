package ru.icc.td.tabbypdf2.comp.util;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Line;
import ru.icc.td.tabbypdf2.model.Word;

import java.awt.*;
import java.util.Set;

public final class FontVerification {

    public static <R extends Block> boolean verify(R rec1, R rec2) {
        Set<Font> fonts = rec2.getFonts();

        for (Font f : rec1.getFonts()) {

            if (fonts.contains(f))
                return true;
        }

        return false;
    }

    public static <R extends Line> boolean verify(R rec1, R rec2) {
        Set<Font> fonts = rec2.getFonts();

        for (Font f : rec1.getFonts()) {

            if (fonts.contains(f))
                return true;
        }

        return false;
    }

    public static <R extends Word> boolean verify(R rec1, R rec2) {
        Set<Font> fonts = rec2.getFonts();

        for (Font f : rec1.getFonts()) {

            if (fonts.contains(f))
                return true;
        }

        return false;
    }
}
