package ru.icc.td.tabbypdf2.comp.util;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public final class Line2DVerification {

    /*public static <L extends Line2D.Double,
            R extends Rectangle2D.Double> boolean verify(R rec1, R rec2, List<L> list, Orientation orientation) {
        boolean condition1;
        boolean condition2;
        double xMin = Math.min(rec1.getMinX(), rec2.getMinX());
        double xMax = Math.max(rec1.getMaxX(), rec1.getMaxX());

        for (Line2D line : list) {

            if (orientation == Orientation.HORIZONTAL && line.getY1() == line.getY2()) {
                condition1 = (rec1.getMinY() > line.getY1() && rec2.getMaxY() < line.getY1()) ||
                        (rec2.getMinY() > line.getY1() && rec1.getMaxY() < line.getY1());
                condition2 =

                if (condition1) {
                    return true;
                }

                continue;
            }

            if (orientation == Orientation.VERTICAL && line.getX1() == line.getX2()) {
                condition1 = (rec1.getMinX() > line.getX1() && rec2.getMaxX() < line.getX1()) ||
                        (rec2.getMinX() > line.getX1() && rec1.getMaxX() < line.getX1());

                if (condition1) {
                    return true;
                }
            }
        }

        return false;
    }*/

    public static <L extends Line2D.Double,
            R extends Rectangle2D.Double> boolean verify(R rec1, R rec2, List<L> list, Orientation orientation) {
        Rectangle2D rec = new Rectangle2D.Double();

        for (Line2D line : list) {

            if (orientation == Orientation.HORIZONTAL && line.getY1() == line.getY2()) {
                double xMin = Math.max(rec1.getMinX(), rec2.getMinX());
                double xMax = Math.min(rec1.getMaxX(), rec2.getMaxX());
                double yMin = Math.min(rec1.getMaxY(), rec2.getMaxY());
                double yMax = Math.max(rec1.getMinY(), rec2.getMinY());

                rec.setRect(xMin, yMin, xMax - xMin, yMax - yMin);

                if (rec.intersectsLine(line)) {
                    return true;
                }
            }

            if (orientation == Orientation.VERTICAL && line.getX1() == line.getX2()) {
                double xMin = Math.min(rec1.getMaxX(), rec2.getMaxX());
                double xMax = Math.max(rec1.getMinX(), rec2.getMinX());
                double yMin = Math.max(rec1.getMinY(), rec2.getMinY());
                double yMax = Math.min(rec1.getMaxY(), rec2.getMaxY());

                rec.setRect(xMin, yMin, xMax - xMin, yMax - yMin);

                if (rec.intersectsLine(line)) {
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
