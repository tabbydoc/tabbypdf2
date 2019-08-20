package ru.icc.td.tabbypdf2.comp.util;

import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

public final class Line2DVerification {

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
