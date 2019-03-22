package ru.icc.td.tabbypdf2.detect.processing.recognition;

import java.awt.geom.Line2D;

class Projection {

    private int level;
    private float start;
    private float end;

    Projection(float start, float end) {
        this(start, end, -1);
    }

    private Projection(float start, float end, int level) {
        this.start = start;
        this.end = end;
        setLevel(level);
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getStart(){
        return start;
    }

    public float getEnd(){
        return end;
    }

    public boolean intersectsProjection(Projection projection){
        boolean areIntersected = Line2D.linesIntersect(0, start, 0, end,
                0, projection.getStart(), 0, projection.getEnd());
        boolean hasMeaning = true;

        if(areIntersected) {
            float start1 = projection.getStart();
            float length1 = projection.getLength();
            float length2 = getLength(end, start1);

            hasMeaning = length2 / length1 >= 0.5;
        }

        return areIntersected && hasMeaning;
    }

    public Projection createIntersection(Projection projection){
        float start = Math.max(getStart(), projection.getStart());
        float end = Math.min(getEnd(), projection.getEnd());

        return new Projection(start, end);
    }

    public float getLength() {
        return getLength(start, end);
    }

    private float getLength(float start, float end){
        return Math.abs(start -= end);
    }

}
