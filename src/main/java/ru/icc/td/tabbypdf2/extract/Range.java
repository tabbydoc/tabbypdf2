package ru.icc.td.tabbypdf2.extract;

import java.util.ArrayList;

public class Range {

    private final double ERROR = 1f;
    private double start;
    private double end;

    public Range(double start, double end){
        this.start = start;
        this.end = end;
    }

    public void setEnd(float end){
        this.end = end;
    }

    public double getEnd(){
        return this.end;
    }

    public void setStart(float start){
        this.start = start;
    }

    public double getStart(){
        return this.start;
    }

    public double length(){
        return getEnd() - getStart();
    }

    public boolean isIntersectionRange(Range range){
        double dist = Math.min(this.end, range.end) - Math.max(this.start, range.start);
        //float d1 = this.end - this.start;
        //float d2 = range.end - range.start;
        //float max = Math.max(d1, d2) / 2;
        //float l = d1 + d2 - dist;
        //if (l > max) return false;
        //float len = (length() + range.length()) / 2;
        if (dist > ERROR) return true;
        return false;
    }

    public int inRange(ArrayList<Range> allRanges){
        int ind = 0;
        for (Range currRange: allRanges){
            if(currRange.getStart() < this.getStart() && currRange.getEnd() > this.getEnd()){
                return ind;
            }
            ind++;
        }
        return 0;
    }

}


