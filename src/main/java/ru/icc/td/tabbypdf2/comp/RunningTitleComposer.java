package ru.icc.td.tabbypdf2.comp;

import org.apache.commons.text.similarity.LevenshteinDistance;
import ru.icc.td.tabbypdf2.model.Line;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Rectangle2D;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class RunningTitleComposer {

    private final LevenshteinDistance levenshteinDistance = new LevenshteinDistance(3);
    private final double maxDelta = 1;

    public void detectTitles(List<Page> pages){
        if (pages.size() < 2) {
            return;
        }

        int pagesCount = pages.size();

        for (int i=0; i < pagesCount - 1; i++) {
            Page page1 = pages.get(i);

            boolean footerFounded = false;
            boolean headerFounded = false;

            for (int j= i + 1; j < pagesCount; j++) {
                Page page2 = pages.get(j);

                if (!footerFounded) {
                    footerFounded = detectPageFooter(page1, page2);
                }

                if (!headerFounded) {
                    headerFounded = detectPageHeader(page1, page2);
                }

                if (footerFounded && headerFounded) break;
            }

        }


    }

    private boolean detectPageFooter(Page page1, Page page2) {

        List<Line> lines1 = page1.getLines();
        List<Line> lines2 = page2.getLines();

        Comparator yxAscending = Comparator.comparing(Rectangle2D::getY);
        Collections.sort(lines1, yxAscending);
        Collections.sort(lines2, yxAscending);

        double page1Footer = page1.getMinY();
        double page2Footer = page2.getMinY();

        boolean founded = false;

        for (int i = 0; i < lines1.size(); i++) {
            Line line1 = lines1.get(i);
            Line line2 = lines2.get(i);

            if (null == line2) break;

            if (levenshteinDistance.apply(line1.getText(), line2.getText()) == -1) {
                break;
            }

            if (line1.getMaxY() > page1Footer) page1Footer = line1.getMaxY();
            if (line2.getMaxY() > page2Footer) page2Footer = line2.getMaxY();

            founded = true;
        }

        if (founded) {
            if (page1.getPageFooter() < page1Footer) page1.setPageFooter(page1Footer);
            if (page2.getPageFooter() < page2Footer) page2.setPageFooter(page2Footer);
        }

        return founded;

    }

    private boolean detectPageHeader(Page page1, Page page2) {
        List<Line> lines1 = page1.getLines();
        List<Line> lines2 = page2.getLines();

        Comparator yxAscending = Comparator.comparing(Rectangle2D::getY).reversed();
        Collections.sort(lines1, yxAscending);
        Collections.sort(lines2, yxAscending);

        double page1Header = page1.getMaxY();
        double page2Header = page2.getMaxY();

        boolean founded = false;

        for (int i = 0; i < lines1.size(); i++) {
            Line line1 = lines1.get(i);
            Line line2 = lines2.get(i);

            if (null == line2) break;

            if (levenshteinDistance.apply(line1.getText(), line2.getText()) == -1) {
                break;
            }

            if (line1.getMinY() < page1Header) page1Header = line1.getMinY();
            if (line2.getMinY() < page2Header) page2Header = line2.getMinY();

            founded = true;
        }

        if (founded) {
            if (page1.getPageHeader() > page1Header) page1.setPageHeader(page2Header);
            if (page2.getPageHeader() > page1Header) page2.setPageHeader(page2Header);
        }

        return founded;
    }
}
