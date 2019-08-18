package ru.icc.td.tabbypdf2.comp.line;

import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.model.Line;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Word;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LineAlgorithm implements Algorithm {

    @Override
    public void start(Page page) {
        double width = page.getWidth();

        List<Word> words = new ArrayList<>(page.getWords());
        List<Word> lineWords = new ArrayList<>();

        Rectangle2D rect = new Rectangle2D.Double();
        Word wordI;
        Word wordJ;

        for (int i = 0; i < words.size(); i++) {
            wordI = words.get(i);
            lineWords.clear();

            lineWords.add(wordI);
            rect.setRect(0d, wordI.y, width, wordI.height);

            for (int j = 0; j < words.size(); j++) {
                wordJ = words.get(j);

                if (rect.intersects(wordJ) && !wordI.equals(wordJ)) {
                    lineWords.add(wordJ);
                }
            }

            page.addLine(new Line(lineWords));
            words.removeAll(lineWords);
            i = -1;
        }
    }
}