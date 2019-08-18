package ru.icc.td.tabbypdf2.model;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Line extends Rectangle2D.Double {
    private final List<Word> words = new ArrayList<>();

    private double minSpaceWidth = 0f;
    private double maxSpaceWidth = 0f;
    private double averageSpaceWidth = 0f;
    private double lineSpace;

    public Line(List<Word> words) {
        this.words.addAll(words);
        assign(this.words);
        setAll();
    }

    public Line(Word word) {
        this.words.add(word);
        assign(this.words);
        setAll();
    }

    private void setAll() {
        double minX = java.lang.Double.MAX_VALUE;
        double minY = java.lang.Double.MAX_VALUE;
        double maxX = java.lang.Double.MIN_VALUE;
        double maxY = java.lang.Double.MIN_VALUE;

        double minSpaceWidth = java.lang.Double.MAX_VALUE;
        double maxSpaceWidth = java.lang.Double.MIN_VALUE;
        double sumSpaceWidth = 0f;

        for (Word word : words) {
            if (word.x < minX)
                minX = word.x;

            if (word.x + word.width > maxX)
                maxX = word.x + word.width;

            minY = Math.min(minY, word.getMinY());
            maxY = Math.max(maxY, word.getMaxY());

            double spaceWidth = word.getAverageSpaceWidth();

            if (spaceWidth < minSpaceWidth)
                minSpaceWidth = spaceWidth;

            if (spaceWidth > maxSpaceWidth)
                maxSpaceWidth = spaceWidth;

            sumSpaceWidth += spaceWidth;
        }

        setRect(minX, minY, maxX - minX, maxY - minY);

        this.minSpaceWidth = minSpaceWidth;
        this.maxSpaceWidth = maxSpaceWidth;
        this.averageSpaceWidth = sumSpaceWidth / words.size();
    }

    public List<Word> getWords() {
        return words;
    }

    public void addWord(Word word) {
        this.words.add(word);
        assign(word);
        setAll();
    }

    public void addWords(List<Word> words) {
        this.words.addAll(words);
        assign(words);
        setAll();
    }

    public void removeWord(Word word) {
        this.words.remove(word);
        reassign(word);
        setAll();
    }

    public void removeWords(List<Word> words) {
        this.words.removeAll(words);
        reassign(words);
        setAll();
    }

    public Set<Font> getFonts() {
        Set<Font> fonts = new HashSet<>();

        words.forEach(word -> fonts.addAll(word.getFonts()));

        return fonts;
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

    private void assign(List<Word> words) {
        words.forEach(word -> assign(word));
    }

    private void assign(Word word) {
        word.setLine(this);
    }

    private void reassign(List<Word> words) {
        words.forEach(word -> reassign(word));
    }

    private void reassign(Word word) {
        word.setLine(null);
    }

    public double getLineSpace() {
        return this.lineSpace;
    }

    public void setLineSpace(double lineSpace) {
        this.lineSpace = lineSpace;
    }
}