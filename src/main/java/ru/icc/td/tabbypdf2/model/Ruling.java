package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public final class Ruling extends Line2D.Float {

    private Page page;

    public Ruling(Point2D startPoint, Point2D endPoint) {
        super(startPoint, endPoint);
    }

    public Ruling(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
    }
}
