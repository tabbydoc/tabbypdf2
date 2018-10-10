package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class Line extends Block {
    private List<Word> words;
    private Block block;

    public Line(List<Word> words) {
        super(words);
    }
}
