package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Block extends Rectangle2D.Float {

    private final List<Word> words;
    private List<Line> lines;

    private float minSpaceWidth = 0f;
    private float maxSpaceWidth = 0f;
    private float averageSpaceWidth = 0f;

    private Page page;

    {
        words = new ArrayList<>(3000);
    }

    public Block(List<Word> words) {
        this.words.addAll(words);
        setAll() ;
    }

    private void setAll() {
        float minX = java.lang.Float.MAX_VALUE;
        float minY = java.lang.Float.MAX_VALUE;
        float maxX = java.lang.Float.MIN_VALUE;
        float maxY = java.lang.Float.MIN_VALUE;

        float minSpaceWidth = java.lang.Float.MAX_VALUE;
        float maxSpaceWidth = java.lang.Float.MIN_VALUE;
        float sumSpaceWidth = 0f;

        for(Word word : words) {
            if(word.x < minX)
                minX = word.x;

            if(word.x + word.width > maxX)
                maxX = word.x + word.width;

            if(word.y < minY)
                minY = word.y;

            if(word.y + word.height > maxY)
                maxY = word.y + word.height;

            float spaceWidth = word.getAverageSpaceWidth();

            if  (spaceWidth < minSpaceWidth)
                minSpaceWidth = spaceWidth;

            if  (spaceWidth > maxSpaceWidth)
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

    public void addWords(Collection<Word> words){
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

    public float getMinSpaceWidth() {
        return minSpaceWidth;
    }

    public float getMaxSpaceWidth() {
        return maxSpaceWidth;
    }

    public float getAverageSpaceWidth() {
        return averageSpaceWidth;
    }
}