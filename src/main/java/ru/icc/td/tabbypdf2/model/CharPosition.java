package ru.icc.td.tabbypdf2.model;

import java.awt.Font;
import java.awt.Color;
import java.awt.geom.Rectangle2D;

public class CharPosition extends Rectangle2D.Float {
    private int chunkId;
    private Character unicode;
    private float spaceWidth;
    private Font font;
    private Color color;

    private Word word;

    public CharPosition(int chunkId, Character unicode, Rectangle2D.Float bbox, float spaceWidth, Font font, Color color) {
        setChunkId(chunkId);
        setUnicode(unicode);
        setSpaceWidth(spaceWidth);
        setFont(font);
        setColor(color);
        setRect(bbox.x, bbox.y, bbox.width, bbox.height);
    }

    private void setChunkId(int id) {
        assert (id > -1);
        chunkId = id;
    }

    public int getChunkId() {
        return chunkId;
    }

    public Character getUnicode() {
        return unicode;
    }

    private void setUnicode(Character unicode) {
        this.unicode = unicode;
    }

    public float getSpaceWidth() {
        return spaceWidth;
    }

    private void setSpaceWidth(float spaceWidth) {
        assert (spaceWidth >= 0f);
        this.spaceWidth = spaceWidth;
    }

    public Font getFont() {
        return font;
    }

    private void setFont(Font font) {
        this.font = font;
    }

    public Color getColor() {
        return color;
    }

    private void setColor(Color color) {
        this.color = color;
    }

    public Word getWord() {
        return word;
    }

    public void setWord(Word word) {
        assert (null != word);
        this.word = word;
    }

}
