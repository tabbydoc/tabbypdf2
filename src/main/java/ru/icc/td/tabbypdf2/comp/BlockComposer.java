package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.*;

import java.awt.geom.Rectangle2D;
import java.util.*;

public class BlockComposer {
    private Page page;

    public void composeBlocks(Page page) {
        words.addAll(page.getWords());
        this.page = page;

        if (words.isEmpty())
            return;

        List<Block> blocks = composeBlocks();
        page.addBlocks(blocks);
    }

    private final List<Word> words = new ArrayList<>(3000);
    private final List<Word> blockWords = new ArrayList<>(250);
    private final List<Block> blocks = new ArrayList<>(400);
    private final List<Block> updatedBlocks = new ArrayList<>(400);

    private List<Block> composeBlocks() {
        if (words.isEmpty())
            return null;

        blocks.clear();
        blockWords.clear();

        Block block;
        Word word;

        for (int i = 0; i < words.size(); i++) {
            word = words.get(i);
            addWord(word);
            i = -1;

            block = new Block(blockWords);

            blocks.add(block);
            blockWords.clear();
        }

        unionIntersectedBlocks();
        unionSeparatedWords();
        unionWronglyIsolatedBlocks();

        separateByChunkId();
        separateByChunkId();

        //unionIntersectedBlocks();
        unionSeparatedWords();
        unionWronglyIsolatedBlocks();

        return blocks;
    }

    /**
     * Убирает слово из списка слов, добавляет его к словам блока и ищет для него пересечения в методе
     * <code>hasWordIntersections</code>
     * @param word Слово, для которого нужно найти пересечения
     */
    private void addWord(Word word) {
        blockWords.add(word);
        words.remove(word);
        hasWordIntersections(word);
    }

    /**Ищет для даного слова <code>word</code> пересечения
     * @param word Слово, для которого ищется пересечение
     */
    private void hasWordIntersections(Word word) {
        Rectangle2D.Float rectangle = new Rectangle2D.Float();
        rectangle.setRect(word.x, (word.y - word.height), word.width, 3 * word.height); //создаём прямоугольник высотой в три высоты слова

        Word wordJ;
        for (int j = 0; j < words.size(); j++) {
            wordJ = words.get(j);

            boolean isOrder = Math.abs(word.getStartChunkID() - wordJ.getStartChunkID()) <= 1;
            boolean isRulings = hasWordLineIntersections(word, wordJ);

            if (rectangle.intersects(wordJ) && isOrder && !isRulings) {
                addWord(wordJ);
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

            if (blockWordsI.size() == 1)
                continue;

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
        for(int i = 0; i < blocks.size(); i++){
            if(blocks.get(i).getWords().size() == 0) {
                blocks.remove(i);
                i--;
            }
        }
    }

    /**Ищет блоки, которые пересекаются и объединяет их
     */
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
        } while (hasIntersections(updatedBlocks));
    }

    /**Объединяем блоки, которые находятся рядом
     */

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

    /**Объдиняет блоки состоящих из одного слова
     */
    private void unionSeparatedWords(){
        Block blockI;
        Block blockJ;
        Rectangle2D.Float rectangle = new Rectangle2D.Float();

        updatedBlocks.clear();

        for(int i = 0; i < blocks.size(); i++){
            blockI = blocks.get(i);

            if(blockI.getWords().size() != 1)
                continue;

            blocks.remove(i);
            rectangle.setRect(0, blockI.y, page.width, blockI.height);
            float idI = blockI.getWords().get(0).getStartChunkID();

            for(int j = 0; j < blocks.size(); j++){
                blockJ = blocks.get(j);

                boolean areIdsEqual = false;

                for(int k = 0; k < blockJ.getWords().size(); k++){
                    Word word = blockJ.getWords().get(k);

                    if(idI == word.getStartChunkID())
                        areIdsEqual = true;
                }

                if(rectangle.intersects(blockJ) && areIdsEqual && !hasLineIntersections(blockI, blockJ)){
                    blocks.remove(j);
                    j--;
                    i = -1;
                    blockI.addWords(blockJ.getWords());
                }
            }
            updatedBlocks.add(blockI);
        }
        blocks.addAll(updatedBlocks);
    }

    /**Ищет максимальный пробел в данном блоке
     * @param block, для которого нужно посчитать пробел
     * @return Возвращает длину пробела
     */
    private float calculateSpace(Block block){
        List<CharPosition> charPositions;
        CharPosition charPosition;
        Word word;
        List<Word> words = block.getWords();
        float space = Float.MIN_VALUE, spaceWidth = Float.MIN_VALUE;

        for(int i = 0; i < words.size(); i++){
            word = words.get(i);
            charPositions = word.getCharPositions();

            for(int j = 0; j < charPositions.size(); j++){
                charPosition = charPositions.get(j);
                spaceWidth = Math.max(charPosition.getSpaceWidth(), spaceWidth);
            }
            space = Math.max(spaceWidth, space);
        }

        return space;
    }

    /**Определяет: блоки, которые изолированы по ошибке
     * @param blocks список, для которого нужно найти такие блоки
     * @return Возвращает true, если есть такие блоки
     */

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

                if (rectangle1.intersects(rectangle2)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**Определяет: есть ли пересекающиеся блоки
     * @param blocks список, в котором это нужно найти
     * @return true, если есть такие блоки
     */
    private boolean hasIntersections(List<Block> blocks) {

        for (Block block1 : blocks) {
            for (Block block2 : blocks) {

                if(block2.equals(block1))
                    continue;

                if (block1.intersects(block2))
                    return true;
            }
        }
        return false;
    }

    private boolean hasWordLineIntersections(Word wordI, Word wordJ){
        List<Ruling> rulings = page.getRulings();

        for (int i = 0; i < rulings.size(); i++) {
            Ruling ruling = rulings.get(i);

            if (ruling.x1 == ruling.x2 && ((wordI.x + wordI.width <= ruling.x1 && ruling.x1 <= wordJ.x)
                    || (wordJ.x + wordJ.width <= ruling.x1 && ruling.x1 <= wordI.x)))
                return true;
        }
        return false;
    }

    /**Определяет: есть ли между двумя блоками вертикальный Ruling
     * @param blockI Первый блок
     * @param blockJ Второй блок
     * @return true, если есть между блоками Ruling
     */
    private boolean hasLineIntersections(Block blockI, Block blockJ) {
        List<Ruling> rulings = page.getRulings();

        for (Ruling ruling : rulings) {
            if (ruling.x1 == ruling.x2 &&
                    ((blockI.x + blockI.width <= ruling.x1 && ruling.x1 <= blockJ.x) ||
                            (blockJ.x + blockJ.width <= ruling.x1 && ruling.x1 <= blockI.x)))
                return true;
        }

        return false;
    }

    /**Пока что в стадии developing.
     * Разибвает в столбцевой блок построчно. Суть описана в статье Кейгера
     */

    private void separateOneToOneRelation(){
        List<Block> blocks1 = new ArrayList<>();
        List<Word> neighborWords = new ArrayList<>();
        Rectangle2D.Float rectangle = new Rectangle2D.Float();
        Word upperWord;
        Word lowerWord;
        Word tempWord;
        Block updatedBlock;

        for(int k = 0; k < blocks.size(); k ++) {
            Block block = blocks.get(k);

            List<Word> words = block.getWords();
            updatedBlocks.clear();

            for (int i = 0; i < words.size(); i++) {
                neighborWords.clear();
                upperWord = words.get(i);
                rectangle.setRect(upperWord.x, (upperWord.y - upperWord.height), upperWord.width, 2 * upperWord.height);

                for (int j = 0; j < words.size(); j++) {
                    tempWord = words.get(j);

                    if (upperWord.equals(tempWord))
                        continue;

                    if (rectangle.intersects(tempWord)) {
                        neighborWords.add(tempWord);
                    }

                    if (neighborWords.size() > 1)
                        break;
                }

                if (neighborWords.size() != 1) {
                    continue;
                }

                lowerWord = neighborWords.get(0);
                rectangle.setRect(lowerWord.x, lowerWord.y, lowerWord.width, 2 * lowerWord.height);
                neighborWords.clear();

                for (int j = 0; j < words.size(); j++) {
                    tempWord = words.get(j);

                    if (tempWord.equals(lowerWord))
                        continue;

                    if (rectangle.intersects(tempWord)) {
                        neighborWords.add(tempWord);
                    }

                    if (neighborWords.size() > 1)
                        break;
                }

                if (neighborWords.size() != 1) {
                    continue;
                }

                tempWord = neighborWords.get(0);

                if (tempWord.equals(upperWord)) {
                    blockWords.clear();
                    blockWords.add(lowerWord);
                    updatedBlock = new Block(blockWords);
                    updatedBlocks.add(updatedBlock);

                    blockWords.clear();
                    blockWords.add(upperWord);
                    updatedBlock = new Block(blockWords);
                    updatedBlocks.add(updatedBlock);

                    blocks.remove(block);
                    k = -1;

                    block.removeWord(upperWord);
                    block.removeWord(lowerWord);
                    updatedBlocks.add(block);
                    i = -1;
                }
            }
            blocks1.addAll(updatedBlocks);
        }

        blocks.addAll(blocks1);
    }
}
