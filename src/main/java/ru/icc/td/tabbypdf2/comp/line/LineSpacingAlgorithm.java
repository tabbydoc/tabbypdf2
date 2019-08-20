package ru.icc.td.tabbypdf2.comp.line;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;
import ru.icc.td.tabbypdf2.comp.util.FontVerification;
import ru.icc.td.tabbypdf2.interfaces.Algorithm;
import ru.icc.td.tabbypdf2.model.Line;
import ru.icc.td.tabbypdf2.model.Page;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class LineSpacingAlgorithm implements Algorithm<Page> {
    private final DescriptiveStatistics statS = new DescriptiveStatistics();
    private final DescriptiveStatistics statH = new DescriptiveStatistics();
    private List<Line> lines;
    private Set<Line> currentLines;

    private int size;

    @Override
    public void start(Page page) {
        setAll(page);

        for (int i = 0; i < size; i++) {
            Line line = lines.get(i);

            if (currentLines.contains(line)) {
                continue;
            }

            currentLines.clear();
            currentLines.add(line);

            statH.clear();
            statS.clear();
            statS.addValue(0d);
            statH.addValue(line.height);

            findNext(line, i);
            assign();
        }
    }

    private void findNext(Line line1, int iterator) {
        for (int i = iterator + 1; i < size; i++) {
            Line line2 = lines.get(i);

            if (currentLines.contains(line2)) {
                continue;
            }

            double s = line1.getMinY() - line2.getMaxY();
            boolean space = s < 4 * statH.getMean();
            boolean font = FontVerification.verify(line1, line2);

            if (font) {
                if (space) {
                    statS.addValue(s);
                }

                statH.addValue(line2.height);

                currentLines.add(line2);
                findNext(line2, i);
            }
        }
    }

    private void assign() {
        double meanS = statS.getMean();
        double meanH = statH.getMean();
        double c = meanS / meanH;

        currentLines.forEach(line -> line.setLineSpace(c));
    }

    private void setAll(Page page) {
        lines = page.getLines();
        lines.sort(Comparator.comparing(Line::getY).reversed());
        size = lines.size();
        currentLines = new HashSet<>();
    }
}