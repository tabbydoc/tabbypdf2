package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Rectangle2D;
import java.util.List;

public class Block extends Rectangle2D.Float {
    private List<Line> lines;
    private Page page;
}
