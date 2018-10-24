package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.CursorTrace;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InterColumnGapExtractor {

    private List<Rectangle2D> obstacles;

    public InterColumnGapExtractor() {
    }

    public void composeGaps(Page page) {
        obstacles = new ArrayList<>();
        obstacles.addAll(page.getBlocks());
        List<Rectangle2D> gaps = findAllGaps();
        if (null != gaps)
            page.setGap(gaps);
    }

    private List<Rectangle2D> findAllGaps() {
        if (obstacles == null && obstacles.isEmpty())
            return null;

        Rectangle2D bBox = getBBox();

        List<CursorTrace> leftCursorTraces = new ArrayList<>();
        List<CursorTrace> rightCursorTraces = new ArrayList<>();
        List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.addAll(obstacles);

        // add left and right bounding lines
        rightCursorTraces.add(new CursorTrace(
                new Point2D.Double(bBox.getMinX(), bBox.getMinY()),
                new Point2D.Double(bBox.getMinX(), bBox.getMaxY())
        ));
        leftCursorTraces.add(new CursorTrace(
                new Point2D.Double(bBox.getMaxX(), bBox.getMinY()),
                new Point2D.Double(bBox.getMaxX(), bBox.getMaxY())
        ));

        // add left and right lines of each rectangle
        for (Rectangle2D currentRect:rectangles) {
            CursorTrace left = getLine(currentRect.getMinX(), currentRect.getMinY(), currentRect.getMaxY());
            CursorTrace right = getLine(currentRect.getMaxX(), currentRect.getMinY(), currentRect.getMaxY());

            if (!leftCursorTraces.contains(left)) {
                leftCursorTraces.add(left);
            }
            if (!rightCursorTraces.contains(right)) {
                rightCursorTraces.add(right);
            }
        }

        List<Rectangle2D> gaps = getGaps(leftCursorTraces, rightCursorTraces);
        gaps.addAll(getHorizontalGaps(gaps));

        return gaps;
    }

    private CursorTrace getLine (Double x, Double y1, Double y2) {
        Rectangle2D bbox = getBBox();

        List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.addAll(obstacles);

        Double minY = bbox.getMinY();
        Double maxY = bbox.getMaxY();

        for (Rectangle2D rectangle:rectangles) {
            if (x > rectangle.getMinX() && x < rectangle.getMaxX()) {
                if (y1 > rectangle.getMaxY()) {
                    if (rectangle.getMaxY() > minY) minY = rectangle.getMaxY();
                }

                if (y2 < rectangle.getMinY()) {
                    if (rectangle.getMinY() < maxY) maxY = rectangle.getMinY();
                }
            }

        }

        return new CursorTrace(new Point2D.Double(x, minY), new Point2D.Double(x, maxY));
    }

    private List<Rectangle2D> getGaps(List<CursorTrace> leftLines, List<CursorTrace> rightLines) {
        List<Rectangle2D> gaps = new ArrayList<>();
        List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.addAll(obstacles);

        leftLines.sort(Comparator.comparing(CursorTrace::getX1));
        rightLines.sort(Comparator.comparing(CursorTrace::getX1));

        for(CursorTrace left:leftLines) {
            for (CursorTrace right:rightLines) {
                if (right.getX1() < left.getX1()) {
                    if (doLinesHaveSameHeight(left, right)) {
                        if (isThereNoObstaclesBetweenLines(left, right)) {
                            gaps.add(new Rectangle2D.Double(right.x1, right.y1, left.x1 - right.x1, right.y2 - right.y1));
                        }
                    }
                }
            }
        }

        return gaps;
    }

    private boolean doLinesHaveSameHeight(CursorTrace left, CursorTrace right) {
        return right.getY1() == left.getY1() && right.getY2() == left.getY2();
    }

    private boolean isThereNoObstaclesBetweenLines(CursorTrace leftLine, CursorTrace rightLine) {
        Double height = Math.abs(leftLine.getY2() - leftLine.getY1());
        Double width = Math.abs(rightLine.getX1() - leftLine.getX1());
        Rectangle2D rectangle = new Rectangle2D.Double(rightLine.getX1(), rightLine.getY1(), width, height);

        List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.addAll(obstacles);

        for (Rectangle2D obstacle:rectangles) {
            if (rectangle.contains(obstacle)) return false;
        }

        return true;
    }

    private Rectangle2D getBBox() {
        if (obstacles == null && obstacles.isEmpty())
            return null;

        if (obstacles.size() == 1)
            return obstacles.get(0).getBounds2D();

        double minX = Double.MAX_VALUE;
        double minY = Double.MAX_VALUE;
        double maxX = Double.MIN_VALUE;
        double maxY = Double.MIN_VALUE;

        for (Rectangle2D obstacle : obstacles) {
            if (minX > obstacle.getMinX()) minX = obstacle.getMinX();
            if (minY > obstacle.getMinY()) minY = obstacle.getMinY();
            if (maxX < obstacle.getMaxX()) maxX = obstacle.getMaxX();
            if (maxY < obstacle.getMaxY()) maxY = obstacle.getMaxY();
        }

        double x = minX;
        double y = minY;
        double w = maxX - minX;
        double h = maxY - minY;

        return new Rectangle2D.Double(x, y, w, h);
    }

    private List<Rectangle2D> getHorizontalGaps(List<Rectangle2D> verticalGaps) {
        List<Rectangle2D> horizontalGaps = new ArrayList<>();

        Rectangle2D bbox = getBBox();

        for (Rectangle2D vertical:verticalGaps) {
            Double verticalTop = vertical.getMinY();
            Double verticalBottom = vertical.getMaxY();

            Double y1 = verticalBottom;
            Double y2 = verticalTop;

            for (Rectangle2D obstacle:obstacles) {
                if (obstacle.getMinY() >= verticalTop && obstacle.getMaxY() <= verticalBottom) {
                    if (y1 > obstacle.getMinY()) y1 = obstacle.getMinY();
                    if (y2 < obstacle.getMaxY()) y2 = obstacle.getMaxY();
                }
            }

            horizontalGaps.add(new Rectangle2D.Double(bbox.getMinX(), verticalTop, bbox.getWidth(), y1 - verticalTop));
            horizontalGaps.add(new Rectangle2D.Double(bbox.getMinX(), y2, bbox.getWidth(), verticalBottom - y2));
        }

        return horizontalGaps;
    }
}
