package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.CharPosition;
import ru.icc.td.tabbypdf2.model.Word;

import static ru.icc.td.tabbypdf2.model.Constants.TOLERANCE;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Float.MAX_VALUE;
import static java.lang.Float.min;

public final class WordComposer {

    public void composeWords(Page page) {
        List<CharPosition> charPositions = page.getCharPositions();

        if (charPositions.isEmpty())
            return;

        List<Word> words = composeWords(charPositions);
        page.addWords(words);
    }

    private final List<CharPosition> wordCharPositions = new ArrayList<>(25);
    private final List<Word> words = new ArrayList<>(400);

    private List<Word> composeWords(List<CharPosition> charPositions) {

        if (charPositions.isEmpty())
            return null;

        words.clear();
        Word word;

        // Calculate an epsilon as the minimum between a minimal space width
        // and minimal width among all char position in the page
        float minSpaceWidth = MAX_VALUE;
        float minCharWidth = MAX_VALUE;
        for (CharPosition charPosition : charPositions) {
            minSpaceWidth = min(charPosition.getSpaceWidth(), minSpaceWidth);
            minCharWidth = min(charPosition.width, minCharWidth);
        }
        float epsilon = min(minSpaceWidth, minCharWidth);

        float x1;
        float x2;
        float y1;
        float y2;

        float maxY1;
        float maxY2;

        float delta;
        int k;

        for (int i = 0; i < charPositions.size(); i++) {

            delta = MAX_VALUE;

            CharPosition charPositionI = charPositions.get(i);

            x1 = charPositionI.x;
            y1 = charPositionI.y;

            maxY1 = charPositionI.y + charPositionI.height;

            k = i;

            // Search for the nearest right char position placed on the same imaginary text line
            for (int j = 0; j < charPositions.size(); j++) {
                if (i == j) continue;

                CharPosition charPositionJ = charPositions.get(j);

                x2 = charPositionJ.x;
                y2 = charPositionJ.y;

                maxY2 = charPositionJ.y + charPositionJ.height;

                float diffMinY = Math.abs(y1 - y2);
                float diffMaxY = Math.abs(maxY1 - maxY2);

                if (diffMinY < TOLERANCE && diffMaxY < TOLERANCE && x2 >= x1 && x2 - x1 <= delta) {
                    delta = x2 - x1;
                    k = j;
                }
            }

            wordCharPositions.add(charPositionI);

            double interCharacterDistance = charPositions.get(k).getMinX() - charPositionI.getMaxX();

            // Check if the word ends here
            if (interCharacterDistance > epsilon || k == i) {
                word = new Word(wordCharPositions);

                // Check if the word is not an itemization or padding text
                if (!isItemizationText(word) && !isPaddingText(word))
                    words.add(word);

                wordCharPositions.clear();
            }
        }
        return words;
    }

    private static final String[] PADDING_CHARACTERS;

    static {
        PADDING_CHARACTERS = new String[]{
                "\u005F", // low line
                "\u0332", // combining low line
                "\uFE4D", // dashed low line
                "\u2013", // en dash
                "\u2014", // em dash
                "\u2012", // figure dash
                "\uFE58", // small em dash
                "\u2212", // minus sign
                "\u2796", // heavy minus sign
                "\u02D7", // modifier letter minus sign
                "\u002D", // hyphen-minus
                "\u2010", // hyphen
                "\u00AD", // soft hyphen
                "\u2011", // non-breaking hyphen
                "\uFF0D", // full width hyphen-minus
                "\uFE63", // small hyphen-minus
                "\u058A", // armenian hyphen
                "\u207B", // superscript minus
        };
    }

    private static final String[] ITEMIZATION_CHARACTERS;

    static {
        ITEMIZATION_CHARACTERS = new String[]{
                "\u2022", // bullet
                "\u2023", // triangular bullet
                "\u25E6", // white bullet
                "\u2043", // hyphen bullet
                "\u204C", // black leftwards bullet
                "\u204D", // black rightwards bullet
                "\u2219", // bullet operator
                "\u00B7", // middle dot
                "\u2024", // one dot leader
                "\u25D8", // inverse bullet
                "\u220E", // end of proof
                "\u25B8", // black right-pointing small triangle
                "\u2027", // hyphenation point
                "\u2043", // hyphen bullet
                "\u25A0", // black square
                "\u25A1", // white square
                "\u25A2", // white square with rounded corners
                "\u25AA", // black small square
                "\u25AB", // white small square
                "\u25CB", // white circle
                "\u25CC", // dotted circle
                "\u25CD", // circle with vertical fill
                "\u25CF", // black circle
                "\u25FB", // white medium square
                "\u25FC", // black medium square
                "\u25FD", // white medium small square
                "\u25FE", // black medium small square
                "\u25CA", // lozenge
                "\u2311", // square lozenge
                "\u28EB", // black lozenge
        };
    }

    private boolean isPaddingText(Word word) {
        String text = word.getText();

        if (null == text || text.isEmpty())
            return false;

        if (text.length() > 5) {
            for (String paddingCharacter : PADDING_CHARACTERS)
                if(text.matches(paddingCharacter.concat("+")))
                    return true;
        }

        return false;
    }

    private boolean isItemizationText(Word word) {
        String text = word.getText();

        if (null == text || text.isEmpty())
            return false;

        if (text.length() == 1) {
            for (String itemizationCharacter : ITEMIZATION_CHARACTERS)
                if (text.equalsIgnoreCase(itemizationCharacter))
                    return true;
        }

        return false;
    }

}
