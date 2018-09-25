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

                if(!word.getText().replace("_", "").isEmpty())
                    words.add(word);

                wordCharPositions.clear();
            }
        }
        return words;
    }

}
