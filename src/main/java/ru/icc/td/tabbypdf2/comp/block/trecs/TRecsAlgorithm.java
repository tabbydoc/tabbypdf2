package ru.icc.td.tabbypdf2.comp.block.trecs;

import ru.icc.td.tabbypdf2.comp.util.FontVerification;
import ru.icc.td.tabbypdf2.comp.util.Line2DVerification;
import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Word;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

import static ru.icc.td.tabbypdf2.comp.util.Line2DVerification.Orientation.HORIZONTAL;
import static ru.icc.td.tabbypdf2.comp.util.Line2DVerification.Orientation.VERTICAL;

public class TRecsAlgorithm implements Algorithm {
    private final List<Word> blockWords = new ArrayList<>();
    private List<Word> words;
    private Page page;

    @Override
    public void start(Page page) {
        setAll(page);

        Word word;
        Block block;

        while (!words.isEmpty()) {
            blockWords.clear();

            word = words.get(0);
            addWord(word);

            block = new Block(blockWords);
            page.addBlock(block);
        }
    }

    private void addWord(Word word) {
        blockWords.add(word);
        words.remove(word);
        findNext(word);
    }

    private void findNext(Word word) {
        double height = word.getLine().height;
        double lineSpace = word.getLine().getLineSpace();

        Rectangle2D rectangle = new Rectangle2D.Double(word.x, word.y - (1 + lineSpace) * height,
                word.width, (3 + 2 * lineSpace) * height);

        Word wordI;
        for (int i = 0; i < words.size(); i++) {
            wordI = words.get(i);

            if (rectangle.intersects(wordI) && checkAll(word, wordI)) {
                addWord(wordI);
                i = -1;
            }
        }
    }

    private boolean checkAll(Word word1, Word word2) {
        boolean order = Math.abs(word1.getStartChunkID() - word2.getStartChunkID()) <= 1;

        boolean rulings = Line2DVerification.verify(word1, word2, page.getRulings(), HORIZONTAL) ||
                Line2DVerification.verify(word1, word2, page.getRulings(), VERTICAL);

        boolean cursorTraces = Line2DVerification.verify(word1, word2, page.getCursorTraces(), VERTICAL);

        boolean fonts = FontVerification.verify(word1, word2);

        return order && !rulings && !cursorTraces && fonts;
    }

    private void setAll(Page page) {
        this.page = page;
        words = new ArrayList<>(page.getWords());
    }
}