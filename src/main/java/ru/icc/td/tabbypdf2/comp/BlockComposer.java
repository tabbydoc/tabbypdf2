package ru.icc.td.tabbypdf2.comp;

import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.icc.td.tabbypdf2.model.*;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.*;

public class BlockComposer {
    private Page page;
    private final int VERTICAL = 0;
    private final int HORIZONTAL = 1;
    private float lineSpace;

    public void composeBlocks(Page page) {
        this.page = page;
        words.addAll(page.getWords());

        if (words.isEmpty())
            return;

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

        //lines.addAll(composeLines());
        //lineSpace = calculateLineSpacing();

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
        unionSeparatedWords();

        separateByChunkId();

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
        //float height = lineSpace;

        rectangle.setRect(word.x, word.y - height, word.width, 3*height);

        Word word1;
        for (int j = 0; j < words.size(); j++) {
            word1 = words.get(j);

            boolean isOrder = Math.abs(word.getStartChunkID() - word1.getStartChunkID()) <= 1;
            boolean isRuling = isThereLine2D(word, word1, page.getHorizontalRulings(), HORIZONTAL);
            //boolean isCursorTrace = isThereLine2D(word, word1, page.getCursorTraces());
            //boolean isThereInterColumnGap = isThereInterColumnGap(word, word1);

            if (rectangle.intersects(word1) && isOrder && (!isRuling)){
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

                spaceI = blockI.getMaxSpaceWidth();
                rectangle1.setRect(blockI.x - spaceI, blockI.y, blockI.width + 2 * spaceI, blockI.height);

                for (int j = 0; j < blocks.size(); j++) {
                    blockJ = blocks.get(j);

                    if (blockJ.equals(blockI))
                        continue;

                    spaceJ = blockJ.getMaxSpaceWidth();
                    rectangle2.setRect(blockJ.x - spaceJ, blockJ.y, blockJ.width + 2 * spaceJ, blockJ.height);

                    if (rectangle1.intersects(rectangle2)) {
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

            spaceI = blockI.getMaxSpaceWidth();
            rectangle1.setRect(blockI.x - spaceI, blockI.y, blockI.width + 2 * spaceI, blockI.height);

            for (int j = 0; j < blocks.size(); j++) {
                blockJ = blocks.get(j);

                if (blockJ.equals(blockI))
                    continue;

                spaceJ = blockJ.getMaxSpaceWidth();
                rectangle2.setRect(blockJ.x - spaceJ, blockJ.y, blockJ.width + 2 * spaceJ, blockJ.height);

                if (rectangle1.intersects(rectangle2)) {
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
                //rectangle.setRect(0, blockI.y, page.width, blockI.height);
                final float w = blockI.getMaxSpaceWidth();
                rectangle.setRect(blockI.x - w, blockI.y, blockI.width + w, blockI.height);

                float idI = blockI.getWords().get(0).getStartChunkID();

                for (int j = 0; j < blocks.size(); j++) {
                    blockJ = blocks.get(j);

                    boolean areIdsEqual = false;

                    for (int k = 0; k < blockJ.getWords().size(); k++) {
                        Word word = blockJ.getWords().get(k);

                        if (idI == word.getStartChunkID())
                            areIdsEqual = true;
                    }

                    if (rectangle.intersects(blockJ) && areIdsEqual && !isThereLine2D(blockI, blockJ, page.getCursorTraces(), VERTICAL)) {
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

            //rectangle.setRect(0, blockI.y, page.width, blockI.height);
            final float w = blockI.getMaxSpaceWidth();
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

                if (rectangle.intersects(blockJ) && areIdsEqual && !isThereLine2D(blockI, blockJ, page.getCursorTraces(), VERTICAL)) {
                   return true;
                }
            }
        }
        return false;
    }

    private <T extends Line2D.Float, S extends Rectangle2D.Float> boolean isThereLine2D(S rec1, S rec2, List<T> list, int param) {
        Rectangle2D rectangle = rec1.createUnion(rec2);
        boolean condition = false;

        for (T line2D : list) {

            if(param == HORIZONTAL)
                condition = line2D.y1 == line2D.y2;
            if(param == VERTICAL)
                condition = line2D.x1 == line2D.x2;

            if (condition && line2D.intersects(rectangle))
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
}