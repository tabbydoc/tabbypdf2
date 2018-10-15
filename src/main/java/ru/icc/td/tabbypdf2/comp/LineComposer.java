package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.Line;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Word;

import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

public class LineComposer {
    private Page page;

    public void composeLines(Page page) {
        this.page = page;
        words.addAll(page.getWords());

        if (words.isEmpty())
            return;

        List<Line> lines = composeLines();
        page.addLines(lines);
    }

    private final List<Line> lines = new ArrayList<>(2000);
    private final List<Word> words = new ArrayList<>(3000);
    private final List<Word> lineWords = new ArrayList<>(250);

    private List<Line> composeLines(){
        lines.clear();
        lineWords.clear();

        Rectangle2D.Float rectangle = new Rectangle2D.Float();
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

        return lines;
    }

}
