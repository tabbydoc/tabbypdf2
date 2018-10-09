package ru.icc.td.tabbypdf2.model;

import ru.icc.td.tabbypdf2.util.Utils;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.*;

public final class Ruling extends Line2D.Float {

    public static int DISTANCE_TOLERANCE = 5;

    private Page page;

    public Ruling(Point2D p1, Point2D p2) {
        super(p1, p2);
    }

    public Ruling(float x1, float y1, float x2, float y2) {
        super(x1, y1, x2, y2);
    }

    public RenderingType getRenderingType() {
        return renderingType;
    }

    public float getLength() {
        return this.getEnd() - this.getStart();
    }

    public void setRenderingType(RenderingType renderingType) {
        this.renderingType = renderingType;
    }

    private enum SOType { VERTICAL, H_RIGHT, H_LEFT }
    public enum RenderingType {UNKNOWN, VISIBLE, INVISIBLE}

    //private Point2D.Float startPoint;
    //private Point2D.Float endPoint;

    private RenderingType renderingType;

    public float getPosition() {
        return (float) (this.isVertical() ? super.getP1().getX() : super.getP2().getY());
    }

    public Point2D.Float getStartPoint() {
        return (Point2D.Float) super.getP1(); // startPoint;
    }

    public Point2D.Float getEndPoint() {
        return (Point2D.Float) super.getP2(); //  endPoint;
    }

    private void setTop(float value) {
        setLine(this.getLeft(), value, this.getRight(), this.getBottom());
    }

    private void setLeft(float value) {
        setLine(value, this.getTop(), this.getRight(), this.getBottom());
    }

    private void setBottom(float value) {
        setLine(this.getLeft(), this.getTop(), this.getRight(), value);
    }

    private void setRight(float value) {
        setLine(this.getLeft(), this.getTop(), value, this.getBottom());
    }

    public void setStart(float value) {
        if (this.isVertical()) {
            this.setTop(value);
        } else if (this.isHorizontal()) {
            this.setLeft(value);
        }
    }

    public void setEnd(float value) {
        if (this.isVertical()) {
            this.setBottom(value);
        } else if (this.isHorizontal()) {
            this.setRight(value);
        }
    }

    public void setStartEnd(float start, float end) {
        if (this.isVertical()) {
            this.setTop(start);
            this.setBottom(end);
        }
        else if (this.isHorizontal()) {
            this.setLeft(start);
            this.setRight(end);
        }
    }

    public float getRight() {
        return this.x2;
    }

    public float getLeft() {
        return this.x1;
    }

    public float getBottom() {
        return this.y2;
    }

    public float getTop() {
        return this.y1;
    }

    public float getStart() {
        return this.isVertical() ? this.getTop() : this.getLeft();
    }

    public float getEnd() {
        return this.isVertical() ? this.getBottom() : this.getRight();
    }

    public boolean nearlyIntersects(Ruling another, int colinearOrParallelExpandAmount) {
        if (this.intersectsLine(another)) {
            return true;
        }

        boolean rv = false;

        if (this.perpendicularTo(another)) {
            rv = this.expand(DISTANCE_TOLERANCE).intersectsLine(another);
        } else {
            rv = this.expand(colinearOrParallelExpandAmount)
                    .intersectsLine(another.expand(colinearOrParallelExpandAmount));
        }

        return rv;
    }

    public boolean perpendicularTo(Ruling other) {
        return this.isVertical() == other.isHorizontal();
    }

    public Ruling expand(float amount) {
        Ruling r = (Ruling) this.clone();
        r.setStart(this.getStart() - amount);
        r.setEnd(this.getEnd() + amount);
        return r;
    }

    public boolean oblique() {
        return !(this.isVertical() || this.isHorizontal());
    }

    public static Map<Point2D, Ruling[]> findIntersections(List<Ruling> horizontals, List<Ruling> verticals) {

        class SortObject {
            protected SOType type;
            protected float position;
            protected Ruling ruling;

            public SortObject(SOType type, float position, Ruling ruling) {
                this.type = type;
                this.position = position;
                this.ruling = ruling;
            }
        }

        List<SortObject> sos = new ArrayList<>();

        TreeMap<Ruling, Boolean> tree = new TreeMap<>(new Comparator<Ruling>() {
            @Override
            public int compare(Ruling o1, Ruling o2) {
                return java.lang.Double.compare(o1.getTop(), o2.getTop());
            }});

        TreeMap<Point2D, Ruling[]> result = new TreeMap<>(new Comparator<Point2D>() {
            @Override
            public int compare(Point2D o1, Point2D o2) {
                if (o1.getY() > o2.getY()) return  1;
                if (o1.getY() < o2.getY()) return -1;
                if (o1.getX() > o2.getX()) return  1;
                if (o1.getX() < o2.getX()) return -1;
                return 0;
            }
        });

        for (Ruling h : horizontals) {
            sos.add(new SortObject(SOType.H_LEFT, h.getLeft() - DISTANCE_TOLERANCE, h));
            sos.add(new SortObject(SOType.H_RIGHT, h.getRight() + DISTANCE_TOLERANCE, h));
        }

        for (Ruling v : verticals) {
            sos.add(new SortObject(SOType.VERTICAL, v.getLeft(), v));
        }

        Collections.sort(sos, new Comparator<SortObject>() {
            @Override
            public int compare(SortObject a, SortObject b) {
                int rv;
                if (Utils.feq(a.position, b.position)) {
                    if (a.type == SOType.VERTICAL && b.type == SOType.H_LEFT) {
                        rv = 1;
                    }
                    else if (a.type == SOType.VERTICAL && b.type == SOType.H_RIGHT) {
                        rv = -1;
                    }
                    else if (a.type == SOType.H_LEFT && b.type == SOType.VERTICAL) {
                        rv = -1;
                    }
                    else if (a.type == SOType.H_RIGHT && b.type == SOType.VERTICAL) {
                        rv = 1;
                    }
                    else {
                        rv = java.lang.Double.compare(a.position, b.position);
                    }
                }
                else {
                    return java.lang.Double.compare(a.position, b.position);
                }
                return rv;
            }
        });

        for (SortObject so : sos) {
            switch(so.type) {
                case VERTICAL:
                    for (Map.Entry<Ruling, Boolean> h : tree.entrySet()) {
                        Point2D i = h.getKey().intersectionPoint(so.ruling);
                        if (i == null) {
                            continue;
                        }
                        result.put(i,
                                new Ruling[] { h.getKey().expand(DISTANCE_TOLERANCE),
                                        so.ruling.expand(DISTANCE_TOLERANCE) });
                    }
                    break;
                case H_RIGHT:
                    tree.remove(so.ruling);
                    break;
                case H_LEFT:
                    tree.put(so.ruling, true);
                    break;
            }
        }

        return result;
    }

    public Point2D intersectionPoint(Ruling other) {
        Ruling this_l = this.expand(DISTANCE_TOLERANCE);
        Ruling other_l = other.expand(DISTANCE_TOLERANCE);
        Ruling horizontal, vertical;

        if (!this_l.intersectsLine(other_l)) {
            return null;
        }

        if (this_l.isHorizontal() && other_l.isVertical()) {
            horizontal = this_l; vertical = other_l;
        } else if (this_l.isVertical() && other_l.isHorizontal()) {
            vertical = this_l; horizontal = other_l;
        } else {
            throw new IllegalArgumentException("lines must be orthogonal, vertical and horizontal");
        }
        return new Point2D.Float(vertical.getLeft(), horizontal.getTop());
    }

    public double length() {
        return Math.sqrt(Math.pow(this.x1 - this.x2, 2) + Math.pow(this.y1 - this.y2, 2));
    }

    public boolean isVertical() {
        return this.length() > 0 && Utils.feq(super.getP1().getX(), super.getP2().getX());
    }

    public boolean isHorizontal() {
        return this.length() > 0 && Utils.feq(super.getP1().getY(), super.getP2().getY());
    }

    public Ruling join(Ruling ruling) {
        return new Ruling((Point2D.Float) this.getP1(), (Point2D.Float) ruling.getP2());
    }

    public double getAngle() {
        double angle = Math.toDegrees(Math.atan2(this.getP2().getY() - this.getP1().getY(),
                this.getP2().getX() - this.getP1().getX()));

        if (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    public void normalize() {

        double angle = this.getAngle();
        if (Utils.within(angle, 0, 1) || Utils.within(angle, 180, 1)) { // almost horizontal
            this.setLine(this.x1, this.y1, this.x2, this.y1);
        }
        else if (Utils.within(angle, 90, 1) || Utils.within(angle, 270, 1)) { // almost vertical
            this.setLine(this.x1, this.y1, this.x1, this.y2);
        }
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Ruling other = (Ruling) obj;
        if (!this.getP1().equals(other.getP1())) {
            return false;
        }
        if (!this.getP2().equals(other.getP2())) {
            return false;
        }
        return true;
    }

}
