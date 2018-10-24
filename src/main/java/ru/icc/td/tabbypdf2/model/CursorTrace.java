package ru.icc.td.tabbypdf2.model;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public final class CursorTrace extends Line2D.Float {

    private Page page;

    public CursorTrace(Point2D p1, Point2D p2) {
        super(p1, p2);
    }

    public CursorTrace(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
    }

    @Override
    public void setLine(double x1, double y1, double x2, double y2) {
        assert (x1 == x2) || (y1 == y2);
        super.setLine(x1, y1, x2, y2);
    }

    public boolean isHorizontal() {
        return y1 == y2;
    }

    public boolean isVertical() {
        return x1 == x2;
    }
}
