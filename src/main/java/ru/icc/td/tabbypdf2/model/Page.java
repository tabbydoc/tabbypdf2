package ru.icc.td.tabbypdf2.model;

import ru.icc.td.tabbypdf2.comp.InterColumnGapExtractor;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class Page extends Rectangle2D.Float {
    private List<CharPosition> charPositions;
    private List<Word> words;
    private List<Word> lines;
    private List<Block> blocks;
    private List<CursorTrace> cursorTraces;
    private List<Rectangle2D> imageBounds;
    private List<Rectangle2D> gaps;
    private List<Ruling> visibleRulings;

    /*********/
    /*private List<CursorTrace> left;
    private List<CursorTrace> right;
    public List<CursorTrace> getLeft() {
        return left;
    }
    public List<CursorTrace> getRight() {
        return right;
    }
    public void addToLeft(List<CursorTrace> lines) {
        left.addAll(lines);
    }
    public void addToRight(List<CursorTrace> lines) {
        right.addAll(lines);
    }*/
    /*********/

    private final Document document;
    private final int index;

    {
        charPositions = new ArrayList<>(2000);
        words = new ArrayList<>(300);
        blocks = new ArrayList<>(30);
        cursorTraces = new ArrayList<>(100);
        imageBounds = new ArrayList<>(5);
        visibleRulings = new ArrayList<>();
        //left = new ArrayList<>();
        //right = new ArrayList<>();
    }

    public Page(Document document, int index, Rectangle2D bbox) {
        this.document = document;
        this.index = index;
        setRect(bbox);
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

    public boolean addCursorTraces(Collection<CursorTrace> cursorTraces) {
        return this.cursorTraces.addAll(cursorTraces);
    }

    public int getIndex() {
        return index;
    }

    public List<CursorTrace> getCursorTraces() {
        return cursorTraces;
    }

    public boolean addImageBounds(Collection<Rectangle2D> imageBounds) {
        return this.imageBounds.addAll(imageBounds);
    }

    public List<Rectangle2D> getImageBounds() {
        return imageBounds;
    }

    public boolean addBlocks(Collection<Block> blocks){
        return this.blocks.addAll(blocks);
    }

    public List<Rectangle2D> getGaps () {
        return gaps;
    }

    public void setGap (List<Rectangle2D> value) {
        this.gaps = value;
    }

    public List<Ruling> getRulings() { return visibleRulings; }

    public void addVisibleRulings(List<Ruling> rulings) { this.visibleRulings.addAll(rulings); }
}
