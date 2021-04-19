package ru.icc.td.tabbypdf2.extract;

public class Segment {

    private int start;
    private int end;
    private int index;

    public int getStart() {
        return this.start;
    }

    public void setIndex(int index){
        this.index = index;
    }

    public int getIndex(){
        return this.index;
    }

    public int getEnd() {
        return this.end;
    }

    public Segment(int start, int end){
        this.start = start;
        this.end = end;
    }

    public boolean isOverlap(Segment other){
        if (this.start <= other.end & this.end >= other.start) {
            return true;
        }
        return false;
    }

    public void join(Segment other){
        this.start = Math.min(this.start, other.start);
        this.end = Math.max(this.end, other.end);
    }

}
