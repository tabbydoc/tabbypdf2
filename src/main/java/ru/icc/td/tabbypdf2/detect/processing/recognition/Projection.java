package ru.icc.td.tabbypdf2.detect.processing.recognition;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import ru.icc.td.tabbypdf2.model.Block;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public abstract class Projection extends Line2D {
    private static Map<Horizontal, List<Vertical>> map;
    double start;
    double end;
    double position;
    int level;

    private Projection(double start, double end, double position) {
        this.start = start;
        this.end = end;
        this.position = position;
        this.level = -1;
    }

    private Projection() {
        this(0, 0, -1);
    }

    public static Map<Horizontal, List<Vertical>> getMap() {
        return map;
    }

    public static void setMap(Map<Horizontal, List<Vertical>> projections) {
        map = new HashMap<>(projections);
    }

    public static int getLevel(Block block) {
        Set<Horizontal> horizontals = map.keySet();

        for (Horizontal horizontal : horizontals) {
            if (horizontal.start <= block.getMinX() && block.getMaxX() <= horizontal.end) {
                List<Vertical> verticals = map.get(horizontal);

                for (Vertical vertical : verticals) {
                    if (vertical.start <= block.getMinY() && block.getMaxY() <= vertical.end) {
                        return vertical.level;
                    }
                }
            }
        }

        return -1;
    }

    public void createUnion(Projection projection) {
        this.start = Math.min(projection.start, this.start);
        this.end = Math.max(projection.end, this.end);
    }

    public double getStart() {
        return start;
    }

    public double getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj instanceof Projection) {
            Projection p = (Projection) obj;

            return new EqualsBuilder().
                    append(start, p.start).
                    append(end, p.end).
                    append(level, p.level).
                    append(position, p.position).
                    isEquals();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(17, 37).
                append(start).
                append(end).
                append(level).
                append(position).
                toHashCode();
    }

    public static class Horizontal extends Projection {
        public Horizontal() {
            super();
        }

        public Horizontal(double start, double end, double position) {
            super(start, end, position);
        }

        @Override
        public double getX1() {
            return start;
        }

        @Override
        public double getY1() {
            return position;
        }

        @Override
        public Point2D getP1() {
            return new Point.Double(start, position);
        }

        @Override
        public double getX2() {
            return end;
        }

        @Override
        public double getY2() {
            return position;
        }

        @Override
        public Point2D getP2() {
            return new Point2D.Double(end, position);
        }

        @Override
        public void setLine(double x1, double y1, double x2, double y2) {
            start = x1;
            end = x2;
            position = y1;
            position = y2;
        }

        @Override
        public Rectangle2D getBounds2D() {
            return null;
        }
    }

    public static class Vertical extends Projection {
        public Vertical() {
            super();
        }

        public Vertical(double start, double end, double position) {
            super(start, end, position);
        }

        @Override
        public double getX1() {
            return position;
        }

        @Override
        public double getY1() {
            return start;
        }

        @Override
        public Point2D getP1() {
            return new Point2D.Double(start, position);
        }

        @Override
        public double getX2() {
            return position;
        }

        @Override
        public double getY2() {
            return end;
        }

        @Override
        public Point2D getP2() {
            return new Point2D.Double(end, position);
        }

        @Override
        public void setLine(double x1, double y1, double x2, double y2) {
            start = y1;
            end = y2;
            position = x1;
            position = x2;
        }

        @Override
        public Rectangle2D getBounds2D() {
            return null;
        }
    }
}