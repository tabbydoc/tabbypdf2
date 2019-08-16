package ru.icc.td.tabbypdf2.comp.block.util;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public final class Line2DVerification {

    public static <L extends Line2D.Double,
            R extends Rectangle2D.Double> boolean verify(R rec1, R rec2, List<L> list, Orientation orientation) {
        boolean condition;

        for (Line2D line : list) {

            if (orientation == Orientation.HORIZONTAL && line.getY1() == line.getY2()) {
                condition = (rec1.getMinY() > line.getY1() && rec2.getMaxY() < line.getY1()) ||
                        (rec2.getMinY() > line.getY1() && rec1.getMaxY() < line.getY1());

                if (condition) {
                    return true;
                }

                continue;
            }

            if (orientation == Orientation.VERTICAL && line.getX1() == line.getX2()) {
                condition = (rec1.getMinX() > line.getX1() && rec2.getMaxX() < line.getX1()) ||
                        (rec2.getMinX() > line.getX1() && rec1.getMaxX() < line.getX1());

                if (condition) {
                    return true;
                }
            }
        }

        return false;
    }

    public enum Orientation {
        HORIZONTAL, VERTICAL
    }
}
