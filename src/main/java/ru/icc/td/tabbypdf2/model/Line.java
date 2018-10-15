package ru.icc.td.tabbypdf2.model;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import org.apache.commons.math3.util.Precision;

import java.util.List;

public class Line extends Block {
    private float space;

    public Line(List<Word> words) {
        super(words);
        space = calculateSpace();
    }

    private float calculateSpace(){
        Word wordI, wordJ;
        List<Word> words = super.getWords();
        float w, space, averageWidth = 0; //maxWidth = java.lang.Float.MIN_VALUE;
        DescriptiveStatistics ds1 = new DescriptiveStatistics();
        DescriptiveStatistics ds2 = new DescriptiveStatistics();

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

            //maxWidth = Math.max(wordI.width, maxWidth);
            ds2.addValue(Precision.round(wordI.width, 1));
            if(space < super.width) {
                ds1.addValue(Precision.round(space, 1));
            }
        }

        //maxWidth = Precision.round(maxWidth, 1);
        space = (float) ds1.getMax();
        averageWidth = (float) Precision.round(ds2.getMean(), 1);

        if(Precision.round(space, 1) > averageWidth)
            space = super.getMaxSpaceWidth();
        else
            space = (float) ds1.getMean();

        return space;
    }

    public float getSpace() {
        return space;
    }
}
