package ru.icc.td.tabbypdf2.extract;

import java.util.ArrayList;
import java.util.ListIterator;

public class TableProjection {

    private ArrayList<Segment> segments = null;

    public TableProjection(){
        this.segments = new ArrayList<>();
    }

    public ArrayList<Segment> getSegments(){
        return this.segments;
    }

    public void sort(){
        segments.sort((Segment s1, Segment s2)->s1.getStart()-s2.getStart());
        for (int i = 0; i < this.segments.size() - 1; i++){
            this.segments.get(i).setIndex(i);
        }
    }

    public void add(Segment segment){
        ListIterator<Segment> segmentIterator = this.segments.listIterator();
        while (segmentIterator.hasNext()){
            Segment s = segmentIterator.next();
            if (segment.isOverlap(s)){
                s.join(segment);
                return;
            }
        }
        segmentIterator.add(segment);
    }

}
