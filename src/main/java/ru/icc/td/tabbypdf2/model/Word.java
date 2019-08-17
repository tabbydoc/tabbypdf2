package ru.icc.td.tabbypdf2.model;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class Word extends Rectangle2D.Double {

    private final List<CharPosition> charPositions;
    private final Set<Font> fonts;
    private String text;

    float minSpaceWidth = 0f;
    float maxSpaceWidth = 0f;
    float averageSpaceWidth = 0f;

    private Line line;

    {
        fonts = new HashSet<>(10);
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
        float height = java.lang.Float.MIN_VALUE;

        float minSpaceWidth = java.lang.Float.MAX_VALUE;
        float maxSpaceWidth = java.lang.Float.MIN_VALUE;
        float sumSpaceWidth = 0f;

        StringBuilder sb = new StringBuilder(charPositions.size());

        for (CharPosition cp : charPositions) {
            cp.setWord(this);
            fonts.add(cp.getFont());

            if (cp.x < minX)
                minX = cp.x;

            if (cp.y < minY)
                minY = cp.y;

            if (cp.x + cp.width > maxX)
                maxX = cp.x + cp.width;

            height = Math.max(height, cp.height);

            float spaceWidth = cp.getSpaceWidth();

            if  (spaceWidth < minSpaceWidth)
                minSpaceWidth = spaceWidth;

            if  (spaceWidth > maxSpaceWidth)
                maxSpaceWidth = spaceWidth;

            sumSpaceWidth += spaceWidth;

            sb.append(cp.getUnicode());
        }

        setRect(minX, minY, maxX - minX, height);
        setText(sb.toString());

        this.minSpaceWidth = minSpaceWidth;
        this.maxSpaceWidth = maxSpaceWidth;
        this.averageSpaceWidth = sumSpaceWidth / charPositions.size();
    }

    private void setText(String text) {
        assert (null != text && !text.isEmpty());
        this.text = text;
    }

    public Set<Font> getFonts() {
        return fonts;
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
