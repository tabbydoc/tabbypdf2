package ru.icc.td.tabbypdf2.comp.block.trecs;

import ru.icc.td.tabbypdf2.model.Word;

import java.awt.*;
import java.util.Set;

final class FontVerification {

    public static boolean verify(Word word1, Word word2) {
        Set<Font> fonts = word2.getFonts();

        for (Font f1 : word1.getFonts()) {

            if (fonts.contains(f1))
                return true;
        }

        return false;
    }
}
