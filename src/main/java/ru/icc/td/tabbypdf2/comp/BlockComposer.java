package ru.icc.td.tabbypdf2.comp;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;
import ru.icc.td.tabbypdf2.model.*;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

public class BlockComposer {
    private Page page;
    private final int VERTICAL = 0;
    private final int HORIZONTAL = 1;
    private float lineCoefficient;


    public void composeBlocks(Page page) {
        this.page = page;
        words.clear();
        lines.clear();

        words.addAll(page.getWords());
        lines.addAll(page.getLines());

        if (words.isEmpty() || lines.isEmpty())
            return;

        lineCoefficient = calculateCoefficient();
        List<Block> blocks = composeBlocks();
        page.addBlocks(blocks);
    }

    private final List<Line> lines = new ArrayList<>(2000);
    private final List<Word> words = new ArrayList<>(3000);
    private final List<Word> blockWords = new ArrayList<>(250);
    private final List<Block> blocks = new ArrayList<>(400);
    private final List<Block> updatedBlocks = new ArrayList<>(400);

    private List<Block> composeBlocks() {
        blocks.clear();
        blockWords.clear();
        updatedBlocks.clear();

        Block block;
        Word word;

        while (!words.isEmpty()) {
            word = words.get(0);
            addWord(word);

            block = new Block(blockWords);

            blocks.add(block);
            blockWords.clear();
        }

        unionIntersectedBlocks();
        unionWronglyIsolatedBlocks();

        separateByChunkId();

        unionSeparatedWords();
        unionWronglyIsolatedBlocks();

        return blocks;
    }

    private void addWord(Word word) {
        blockWords.add(word);
        words.remove(word);
        hasWordIntersections(word);
    }

    private void hasWordIntersections(Word word) {
        Rectangle2D.Float rectangle = new Rectangle2D.Float();
        float height = word.height;

        rectangle.setRect(word.x, word.y - lineCoefficient * height, word.width, (1 + 2 * lineCoefficient) * height);

        Word word1;
        for (int j = 0; j < words.size(); j++) {
            word1 = words.get(j);

            boolean isOrder = Math.abs(word.getStartChunkID() - word1.getStartChunkID()) <= 1;
            boolean isRuling = isThereLine2D(word, word1, page.getRulings(), HORIZONTAL) ||
                    isThereLine2D(word, word1, page.getRulings(), VERTICAL);
            boolean isCursorTrace = isThereLine2D(word, word1, page.getCursorTraces(), VERTICAL);
            boolean isFont = areTheSameFonts(word1, word);

            if (rectangle.intersects(word1) && isOrder && !isRuling && !isCursorTrace && isFont){
                addWord(word1);
                j = -1;
            }
        }
    }

    private void separateByChunkId() {
        List<Word> blockWordsI = new ArrayList<>();
        Block blockI;
        Word wordI;
        int chunkIdI;

        List<Word> blockWordsK = new ArrayList<>();
        Block blockK;
        Word wordK;
        int chunkIdK;

        updatedBlocks.clear();
        Block block;

        for (int i = 0; i < blocks.size(); i++) {
            blockWordsI.clear();
            blockI = blocks.get(i);
            blockWordsI.addAll(blockI.getWords());

            for (int j = 0; j < blockWordsI.size(); j++) {

                wordI = blockWordsI.get(j);
                chunkIdI = wordI.getStartChunkID();

                for (int k = 0; k < blocks.size(); k++) {
                    blockWordsK.clear();
                    blockK = blocks.get(k);

                    if (blockK.equals(blockI))
                        continue;

                    blockWordsK.addAll(blockK.getWords());

                    for (int l = 0; l < blockWordsK.size(); l++) {
                        wordK = blockWordsK.get(l);
                        chunkIdK = wordK.getStartChunkID();

                        if (chunkIdI == chunkIdK) {
                            blockI.removeWord(wordI);
                            blockK.removeWord(wordK);

                            blockWords.clear();
                            blockWords.add(wordI);
                            block = new Block(blockWords);
                            updatedBlocks.add(block);

                            blockWords.clear();
                            blockWords.add(wordK);
                            block = new Block(blockWords);
                            updatedBlocks.add(block);

                            blockWordsK.remove(wordK);
                            blockWordsI.remove(wordI);
                            l = -1;
                            j = -1;
                        }
                    }
                }
            }
        }
        blocks.addAll(updatedBlocks);

        for (int i = 0; i < blocks.size(); i++) {
            if (blocks.get(i).getWords().size() == 0) {
                blocks.remove(i);
                i--;
            }
        }
    }

    private void unionIntersectedBlocks() {
        Block blockI;
        Block blockJ;

        do {
            updatedBlocks.clear();

            for (int i = 0; i < blocks.size(); i++) {
                blockI = blocks.get(i);

                blocks.remove(blockI);
                i--;

                for (int j = 0; j < blocks.size(); j++) {
                    blockJ = blocks.get(j);

                    if (blockI.intersects(blockJ)) {
                        blocks.remove(blockJ);
                        j--;
                        i = -1;
                        blockI.addWords(blockJ.getWords());
                    }
                }
                updatedBlocks.add(blockI);
            }

            blocks.clear();
            blocks.addAll(updatedBlocks);
        } while (hasIntersectedBlocks(updatedBlocks));
    }

    private boolean hasIntersectedBlocks(List<Block> blocks) {
        for (Block blockI : blocks) {
            for (Block blockJ : blocks) {
                if (blockI.intersects(blockJ) && !blockI.equals(blockJ)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void unionWronglyIsolatedBlocks() {
        updatedBlocks.clear();
        blockWords.clear();

        Block blockI;
        Block blockJ;
        Rectangle2D.Float rectangle1 = new Rectangle2D.Float();
        Rectangle2D.Float rectangle2 = new Rectangle2D.Float();
        float spaceI, spaceJ;

        do {
            updatedBlocks.clear();

            for (int i = 0; i < blocks.size(); i++) {
                blockI = blocks.get(i);

                spaceI = calculateSpace(blockI);
                rectangle1.setRect(blockI.x - spaceI, blockI.y, blockI.width + 2 * spaceI, blockI.height);

                for (int j = 0; j < blocks.size(); j++) {
                    blockJ = blocks.get(j);

                    if (blockJ.equals(blockI))
                        continue;

                    spaceJ = calculateSpace(blockJ);
                    rectangle2.setRect(blockJ.x - spaceJ, blockJ.y, blockJ.width + 2 * spaceJ, blockJ.height);

                    boolean isRuling = isThereLine2D(blockI, blockJ, page.getRulings(), HORIZONTAL) ||
                            isThereLine2D(blockI, blockJ, page.getRulings(), VERTICAL);
                    boolean isCursorTrace = isThereLine2D(blockI, blockJ, page.getCursorTraces(), VERTICAL);

                    if (rectangle1.intersects(rectangle2) && !isRuling && !isCursorTrace) {
                        blocks.remove(j);
                        i = -1;
                        j--;
                        blockI.addWords(blockJ.getWords());
                    }
                }
                updatedBlocks.add(blockI);
                blocks.remove(blockI);
            }

            blocks.addAll(updatedBlocks);
        } while (hasWronglyIsolatedBlocks(updatedBlocks));
    }

    private boolean hasWronglyIsolatedBlocks(List<Block> blocks) {
        Block blockI;
        Block blockJ;
        Rectangle2D.Float rectangle1 = new Rectangle2D.Float();
        Rectangle2D.Float rectangle2 = new Rectangle2D.Float();
        float spaceI, spaceJ;

        for (int i = 0; i < blocks.size(); i++) {
            blockI = blocks.get(i);

            spaceI = calculateSpace(blockI);
            rectangle1.setRect(blockI.x - spaceI, blockI.y, blockI.width + 2 * spaceI, blockI.height);

            for (int j = 0; j < blocks.size(); j++) {
                blockJ = blocks.get(j);

                if (blockJ.equals(blockI))
                    continue;

                spaceJ = calculateSpace(blockJ);
                rectangle2.setRect(blockJ.x - spaceJ, blockJ.y, blockJ.width + 2 * spaceJ, blockJ.height);

                boolean isRuling = isThereLine2D(blockI, blockJ, page.getRulings(), HORIZONTAL) ||
                        isThereLine2D(blockI, blockJ, page.getRulings(), VERTICAL);
                boolean isCursorTrace = isThereLine2D(blockI, blockJ, page.getCursorTraces(), VERTICAL);

                if (rectangle1.intersects(rectangle2) && !isRuling && !isCursorTrace) {
                    return true;
                }
            }
        }
        return false;
    }

    private void unionSeparatedWords(){
        Block blockI;
        Block blockJ;
        Rectangle2D.Float rectangle = new Rectangle2D.Float();

        do {
            updatedBlocks.clear();

            for (int i = 0; i < blocks.size(); i++) {
                blockI = blocks.get(i);

                if (blockI.getWords().size() != 1)
                    continue;

                blocks.remove(i);

                float w = calculateSpace(blockI);
                rectangle.setRect(blockI.x - w, blockI.y, blockI.width + 2*w, blockI.height);

                float idI = blockI.getWords().get(0).getStartChunkID();

                for (int j = 0; j < blocks.size(); j++) {
                    blockJ = blocks.get(j);

                    if(blockJ.getWords().size() != 1)
                        continue;

                    boolean areIdsEqual = false;

                    for (int k = 0; k < blockJ.getWords().size(); k++) {
                        Word word = blockJ.getWords().get(k);

                        if (idI == word.getStartChunkID())
                            areIdsEqual = true;
                    }

                    boolean isRuling = isThereLine2D(blockI, blockJ, page.getRulings(), HORIZONTAL) ||
                            isThereLine2D(blockI, blockJ, page.getRulings(), VERTICAL);
                    boolean isCursorTrace = isThereLine2D(blockI, blockJ, page.getCursorTraces(), VERTICAL);

                    if (rectangle.intersects(blockJ) && areIdsEqual && !isRuling && !isCursorTrace) {
                        blocks.remove(j);
                        j--;
                        i = -1;
                        blockI.addWords(blockJ.getWords());
                    }
                }
                updatedBlocks.add(blockI);
            }
            blocks.addAll(updatedBlocks);
        } while(hasSeparatedWords(updatedBlocks));
    }

    private boolean hasSeparatedWords(List<Block> blocks){
        Block blockI;
        Block blockJ;
        Rectangle2D.Float rectangle = new Rectangle2D.Float();

        for (int i = 0; i < blocks.size(); i++) {
            blockI = blocks.get(i);

            if (blockI.getWords().size() != 1)
                continue;

            float w = calculateSpace(blockI);

            rectangle.setRect(blockI.x - w, blockI.y, blockI.width + w, blockI.height);

            float idI = blockI.getWords().get(0).getStartChunkID();

            for (int j = 0; j < blocks.size(); j++) {
                blockJ = blocks.get(j);

                if(blockI.equals(blockJ))
                    continue;

                boolean areIdsEqual = false;

                for (int k = 0; k < blockJ.getWords().size(); k++) {
                    Word word = blockJ.getWords().get(k);

                    if (idI == word.getStartChunkID())
                        areIdsEqual = true;
                }
                boolean isRuling = isThereLine2D(blockI, blockJ, page.getRulings(), HORIZONTAL) ||
                        isThereLine2D(blockI, blockJ, page.getRulings(), VERTICAL);
                boolean isCursorTrace = isThereLine2D(blockI, blockJ, page.getCursorTraces(), VERTICAL);

                if (rectangle.intersects(blockJ) && areIdsEqual && !isRuling && !isCursorTrace) {
                   return true;
                }
            }
        }
        return false;
    }

    private float calculateSpace(Block block){
        DescriptiveStatistics ds = new DescriptiveStatistics();

        for (Word aWb : block.getWords()) {

            for (Line line : lines) {
                List<Word> lw = line.getWords();

                if (lw.contains(aWb))
                    ds.addValue(line.getSpace());
            }
        }

        return (float) ds.getMean();
    }

    private <T extends Line2D.Float, S extends Rectangle2D.Float> boolean isThereLine2D(S rec1, S rec2, List<T> list, int orientation) {
        boolean condition = false;
        boolean isIntersected = false;

        for (T line2D : list) {

            if(orientation == HORIZONTAL) {
                condition = line2D.y1 == line2D.y2;

                isIntersected = (rec1.getMaxY() < line2D.y1 && line2D.y1 < rec2.getMinY()) ||
                        (rec2.getMaxY() < line2D.y1 && line2D.y1 < rec1.getMinY());
            }

            if(orientation == VERTICAL) {
                condition = line2D.x1 == line2D.x2;

                isIntersected = (rec1.getMaxX() < line2D.x1 && line2D.x1 < rec2.getMinX()) ||
                        (rec2.getMaxX() < line2D.x1 && line2D.x1 < rec1.getMinX());
            }


            if (condition && isIntersected)
                return true;
        }

        return false;
    }

    private boolean isThereInterColumnGap(Word word1, Word word2){
        List<Rectangle2D> gaps = page.getGaps();

        if(gaps != null) {

            for (Rectangle2D gap : gaps) {

                if ((word1.x + word1.width <= word2.x && gap.getMaxX() < word2.x && word1.x + word1.width >= gap.getMinX()) ||
                        (word2.x + word2.width <= word1.x && gap.getMaxX() < word1.x && word2.x + word2.width >= gap.getMinX()))
                    return true;
            }
        }
        return false;
    }

    private float calculateCoefficient(){
        Line line1, line2;
        float space, t, y1, y2, height2;
        DescriptiveStatistics ds = new DescriptiveStatistics();
        List<Float> cFloats = new ArrayList<>();

        for(int i = 0; i < lines.size(); i++){
            line1 = lines.get(i);
            y1 = line1.y;

            space = Math.abs(page.height);

            for(int j = 0; j < lines.size(); j++){
                line2 = lines.get(j);
                y2 = line2.y;
                height2 = line2.height;

                if(line1.equals(line2) || !(y1 > y2 + height2))
                    continue;

                t = y1 - (y2 + height2);

                if(t < space)
                    space = t;
            }

            if(space < Math.abs(page.height)) {
                //space = Precision.round(space, 2);
                cFloats.add(space/line1.height);
            }

        }

        for(Float f : cFloats)
            ds.addValue(f);

        return (float) ds.getMean();
    }

    private boolean areTheSameFonts(Word word1, Word word2){
        Set<Font> fonts1 = word1.getFonts();
        Set<Font> fonts2 = word2.getFonts();

        for(Font f1 : fonts1){

            if(fonts2.contains(f1))
                return true;

        }

        return false;
    }
}