package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public final class Word extends Rectangle2D.Float {

    private final List<CharPosition> charPositions;
    private String text;

    float minSpaceWidth = 0f;
    float maxSpaceWidth = 0f;
    float averageSpaceWidth = 0f;

    private Line line;

    {
        charPositions = new ArrayList<>(10);
    }

    public Word(List<CharPosition> charPositions) {
        this.charPositions.addAll(charPositions);
        setAll();
    }

    private void setAll() {
        float minX = java.lang.Float.MAX_VALUE;
        float minY = java.lang.Float.MAX_VALUE;
        float maxX = java.lang.Float.MIN_VALUE;
        float maxY = java.lang.Float.MIN_VALUE;

        float minSpaceWidth = java.lang.Float.MAX_VALUE;
        float maxSpaceWidth = java.lang.Float.MIN_VALUE;
        float sumSpaceWidth = 0f;

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

            float spaceWidth = cp.getSpaceWidth();

            if  (spaceWidth < minSpaceWidth)
                minSpaceWidth = spaceWidth;

            if  (spaceWidth > maxSpaceWidth)
                maxSpaceWidth = spaceWidth;

            sumSpaceWidth += spaceWidth;

            sb.append(cp.getUnicode());
        }

        setRect(minX, minY, maxX - minX, maxY - minY);
        setText(sb.toString());

        this.minSpaceWidth = minSpaceWidth;
        this.maxSpaceWidth = maxSpaceWidth;
        this.averageSpaceWidth = sumSpaceWidth / charPositions.size();
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

    public float getMaxSpaceWidth() {
        return maxSpaceWidth;
    }

    public float getMinSpaceWidth() {
        return minSpaceWidth;
    }

    public float getAverageSpaceWidth() {
        return averageSpaceWidth;
    }
}
