package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class Word extends Rectangle2D.Float {
    private String text;
    private final List<CharPosition> charPositions;
    private Line line;

    {
        charPositions = new ArrayList<>(10);
    }

    public Word(List<CharPosition> charPositions) {
        this.charPositions.addAll(charPositions);
        update();
    }

    private void update() {
        float minX = java.lang.Float.MAX_VALUE;
        float minY = java.lang.Float.MAX_VALUE;
        float maxX = java.lang.Float.MIN_VALUE;
        float maxY = java.lang.Float.MIN_VALUE;

        StringBuilder sb = new StringBuilder(charPositions.size());

        for (CharPosition cp : charPositions) {
            cp.setWord(this);

            if (cp.x < minX)
                minX = cp.x;

            if (cp.y < minY)
                minY = cp.y;

            if (cp.x + cp.width > maxX)
                maxX = cp.x + cp.width;

            if (cp.y + cp.height > maxY)
                maxY = cp.y + cp.height;

            sb.append(cp.getUnicode());
        }

        setRect(minX, minY, maxX - minX, maxY - minY);
        setText(sb.toString());
    }

    private void setText(String text) {
        assert (null != text && !text.isEmpty());
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public List<CharPosition> getCharPositions() {
        return charPositions;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Line getLine() {
        return line;
    }

    public int getStartChunkID() {
        return charPositions.get(0).getChunkId();
    }
}
