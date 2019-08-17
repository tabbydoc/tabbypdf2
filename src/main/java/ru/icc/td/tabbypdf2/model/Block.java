package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.*;

public class Block extends Rectangle2D.Double {
    private final List<Word> words;

    private double minSpaceWidth = 0f;
    private double maxSpaceWidth = 0f;
    private double averageSpaceWidth = 0f;

    {
        words = new ArrayList<>(3000);
    }

    public Block(){}

    public Block(List<Word> words) {
        this.words.addAll(words);
        setAll();
    }

    private void setAll() {
        double minX = java.lang.Double.MAX_VALUE;
        double minY = java.lang.Double.MAX_VALUE;
        double maxX = java.lang.Double.MIN_VALUE;
        double height = java.lang.Double.MIN_VALUE;

        double minSpaceWidth = java.lang.Double.MAX_VALUE;
        double maxSpaceWidth = java.lang.Double.MIN_VALUE;
        double sumSpaceWidth = 0f;

        for (Word word : words) {
            if (word.x < minX)
                minX = word.x;

            if (word.x + word.width > maxX)
                maxX = word.x + word.width;

            if (word.y < minY)
                minY = word.y;

            height = Math.max(height, word.height);

            double spaceWidth = word.getAverageSpaceWidth();

            if (spaceWidth < minSpaceWidth)
                minSpaceWidth = spaceWidth;

            if (spaceWidth > maxSpaceWidth)
                maxSpaceWidth = spaceWidth;

            sumSpaceWidth += spaceWidth;
        }

        setRect(minX, minY, maxX - minX, height);

        this.minSpaceWidth = minSpaceWidth;
        this.maxSpaceWidth = maxSpaceWidth;
        this.averageSpaceWidth = sumSpaceWidth / words.size();
    }

    public List<Word> getWords() {
        return words;
    }

    public void addWords(Collection<Word> words) {
        this.words.addAll(words);
        setAll();
    }

    public void removeWord(Word word) {
        words.remove(word);
        setAll();
    }

    public void removeWords(Collection<Word> words) {
        this.words.removeAll(words);
        setAll();
    }

    public double getMinSpaceWidth() {
        return minSpaceWidth;
    }

    public double getMaxSpaceWidth() {
        return maxSpaceWidth;
    }

    public double getAverageSpaceWidth() {
        return averageSpaceWidth;
    }

    public List<Block> findNeighbours(List<Block> blocks) {
        return findNeighbours(this, blocks);
    }

    public static List<Block> findNeighbours(Block block, List<Block> blocks) {
        List<Block> neighbours = new ArrayList<>();
        Block blockI;
        Block blockJ;
        boolean isThereUp;

        double yMin = Collections.min(blocks, Comparator.comparing(Block::getMinY)).getMinY();

        for (int i = 0; i < blocks.size(); i++) {
            isThereUp = false;
            blockI = blocks.get(i);

            if (blockI.equals(block)) {
                continue;
            }

            double xMin = Math.max(block.getMinX(), blockI.getMinX());
            double xMax = Math.min(block.getMaxX(), blockI.getMaxX());

            Rectangle2D rectangle2D = new Rectangle2D.Double(xMin, blockI.getMinY(),
                    xMax - xMin, Math.abs(blockI.getMinY() - block.getMaxY()));

            for (int j = 0; j < blocks.size(); j++) {
                blockJ = blocks.get(j);

                if (blockI.equals(blockJ) || blockJ.equals(block)) {
                    continue;
                }

                if (rectangle2D.intersects(blockJ)) {
                    isThereUp = true;
                    break;
                }

            }

            if (isThereUp) {
                continue;
            }

            rectangle2D.setRect(block.getMinX(), yMin, block.getWidth(), Math.abs(block.getMaxY() - yMin));

            if (blockI.intersects(rectangle2D) && !neighbours.contains(blockI)) {
                neighbours.add(blockI);
            }
        }

        return neighbours;
    }
}