package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Block extends Rectangle2D.Float {
    private List<Line> lines;
    private Page page;
    private final List<Word> words;

    {
        words = new ArrayList<>(3000);
    }

    public Block(List<Word> words){
        this.words.addAll(words);
        update() ;
    }

    private void update(){
        double minX = java.lang.Float.MAX_VALUE;
        double minY = java.lang.Float.MAX_VALUE;
        double maxX = java.lang.Float.MIN_VALUE;
        double maxY = java.lang.Float.MIN_VALUE;

        for(Word word : words){
            if(word.x < minX)
                minX = word.x;

            if(word.x + word.width > maxX)
                maxX = word.x + word.width;

            if(word.y < minY)
                minY = word.y;

            if(word.y + word.height > maxY)
                maxY = word.y + word.height;
        }

        setRect(minX, minY, maxX - minX, maxY - minY);
    }

    public List<Word> getWords() {
        return words;
    }

    public void addWords(List<Word> words){
        this.words.addAll(words);
        update();
    }

    public void removeWord(Word word){
        words.remove(word);
        update();
    }

    public void removeWords(List<Word> words){
        for(Word word : words)
            this.words.remove(word);
        update();
    }
}