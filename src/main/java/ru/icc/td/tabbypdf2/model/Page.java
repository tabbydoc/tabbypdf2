package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Page extends Rectangle2D.Float {
    private List<CharPosition> charPositions;
    private List<Word> words;
    private List<Word> lines;
    private List<Block> blocks;
    private List<Ruling> rulings;
    private List<Ruling> imageBounds;

    private final Document document;
    private final int index;

    {
        charPositions = new ArrayList<>(2000);
        words = new ArrayList<>(300);
        blocks = new ArrayList<>(30);
        rulings = new ArrayList<>(100);
    }

    public Page(Document document, int index, Rectangle2D.Float bbox) {
        this.document = document;
        this.index = index;
        setRect(bbox.x, bbox.y, bbox.width, bbox.height);
    }

    public boolean addCharPositions(Collection<CharPosition> charPositions) {
        return this.charPositions.addAll(charPositions);
    }

    public List<CharPosition> getCharPositions() {
        return charPositions;
    }

    public boolean addWords(Collection<Word> words) {
        return this.words.addAll(words);
    }

    public List<Word> getWords() {
        return words;
    }

    public List<Block> getBlocks() {
        return blocks;
    }

    public boolean addRulings(Collection<Ruling> rulings) {
        return this.rulings.addAll(rulings);
    }

    public int getIndex() {
        return index;
    }

    public List<Ruling> getRulings() {
        return rulings;
    }
}