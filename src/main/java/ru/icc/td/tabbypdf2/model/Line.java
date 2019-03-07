package ru.icc.td.tabbypdf2.model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;

import java.util.ArrayList;
import java.util.List;

public class Line extends Block {
    private double space;

    public Line(List<Word> words) {
        super(words);
        this.space = calculateSpace();
    }

    private double calculateSpace(){
        Word wordI, wordJ;
        List<Word> words = super.getWords();
        double w, space, maxWidth = java.lang.Float.MIN_VALUE;
        DescriptiveStatistics ds = new DescriptiveStatistics();

        for(int i = 0; i < words.size(); i++) {
            space = super.width;
            wordI = words.get(i);
            float xI = wordI.x + wordI.width;

            for(int j = 0; j < words.size(); j++){
                wordJ = words.get(j);

                if(wordJ.equals(wordI) && !(xI < wordJ.x))
                    continue;

                w = wordJ.x - xI;

                if(w < space && w > 0)
                    space = w;
            }

            maxWidth = Math.max(wordI.width, maxWidth);

            if(space < super.width)
                ds.addValue(Precision.round(space, 1));
        }

        space = ds.getMax();

        if(space > maxWidth)
            space = super.getMaxSpaceWidth();
        else
            space = ds.getMean();

        return space;
    }

    public double getSpace() {
        return space;
    }
}
