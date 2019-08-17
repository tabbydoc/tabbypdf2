package ru.icc.td.tabbypdf2.comp.line;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.icc.td.tabbypdf2.model.Line;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Word;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LineComposerOld {
    private Page page;

    public void composeLines(Page page) {
        this.page = page;
        words.addAll(page.getWords());

        if (words.isEmpty())
            return;

        page.addLines(composeLines());
    }

    private final List<Line> lines = new ArrayList<>(2000);
    private final List<Word> words = new ArrayList<>(3000);
    private final List<Word> lineWords = new ArrayList<>(250);

    private List<Line> composeLines(){
        lines.clear();
        lineWords.clear();

        Rectangle2D.Double rectangle = new Rectangle2D.Double();
        Word wordI, wordJ;
        Line line;

        for(int i = 0; i < words.size(); i++){
            lineWords.clear();
            wordI = words.get(i);
            rectangle.setRect(0, wordI.y, this.page.width, wordI.height);
            lineWords.add(wordI);

            words.remove(i);
            i--;

            for(int j = 0; j < words.size(); j++){
                wordJ = words.get(j);

                if(wordI.equals(wordJ))
                    continue;

                if(rectangle.intersects(wordJ)){
                    lineWords.add(wordJ);
                    words.remove(j);
                    j--;
                }
            }
            line = new Line(lineWords);
            lines.add(line);
        }

        page.setLineCoefficient(calculateCoefficient());

        return lines;
    }

    private double calculateCoefficient() {
        Line line1, line2;
        double space, t, y1, y2, height2;
        DescriptiveStatistics ds = new DescriptiveStatistics();
        List<Double> cDoubles = new ArrayList<>();

        for (int i = 0; i < lines.size(); i++) {
            line1 = lines.get(i);
            y1 = line1.y;

            space = Math.abs(page.height);

            for (int j = 0; j < lines.size(); j++) {
                line2 = lines.get(j);
                y2 = line2.y;
                height2 = line2.height;

                if (line1.equals(line2) || !(y1 > y2 + height2))
                    continue;

                t = y1 - (y2 + height2);

                if (t < space)
                    space = t;
            }

            if (space < Math.abs(page.height)) {
                //space = Precision.round(space, 2);
                cDoubles.add(space / line1.height);
            }

        }

        for (Double f : cDoubles)
            ds.addValue(f);

        return ds.getMean();
    }
}
