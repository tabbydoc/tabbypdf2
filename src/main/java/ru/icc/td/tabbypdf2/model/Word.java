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

    double minSpaceWidth = 0f;
    double maxSpaceWidth = 0f;
    double averageSpaceWidth = 0f;

    private Line line;
    private Block block;

    {
        fonts = new HashSet<>(10);
        charPositions = new ArrayList<>(10);
    }

    public Word(List<CharPosition> charPositions) {
        this.charPositions.addAll(charPositions);
        setAll();
    }

    private void setAll() {
        double minX = java.lang.Float.MAX_VALUE;
        double minY = java.lang.Float.MAX_VALUE;
        double maxX = java.lang.Float.MIN_VALUE;
        double maxY = java.lang.Float.MIN_VALUE;

        double minSpaceWidth = java.lang.Float.MAX_VALUE;
        double maxSpaceWidth = java.lang.Float.MIN_VALUE;
        double sumSpaceWidth = 0f;

        StringBuilder sb = new StringBuilder(charPositions.size());

        for (CharPosition cp : charPositions) {
            cp.setWord(this);
            fonts.add(cp.getFont());

            if (cp.x < minX)
                minX = cp.x;

            minY = Math.min(minY, cp.getMinY());

            if (cp.x + cp.width > maxX)
                maxX = cp.x + cp.width;

            maxY = Math.max(maxY, cp.getMaxY());

            double spaceWidth = cp.getSpaceWidth();

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

    public Block getBlock() {
        return block;
    }

    public void setBlock(Block block) {
        this.block = block;
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

    public double getMaxSpaceWidth() {
        return maxSpaceWidth;
    }

    public double getMinSpaceWidth() {
        return minSpaceWidth;
    }

    public double getAverageSpaceWidth() {
        return averageSpaceWidth;
    }
}
