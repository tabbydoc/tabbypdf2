package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.CursorTrace;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class InterColumnGapExtractor {

    private final Comparator<Rectangle2D.Float> RECTANGLE_COMPARATOR = Comparator
            .comparing(Rectangle2D.Float::getY)
            .reversed()
            .thenComparing(Rectangle2D.Float::getX);
    //private List<CursorTrace> cursorTraces;
    //private Tuple<List<CursorTrace>> linesTuple;
    private List<Rectangle2D> obstacles;

    /*
    private class Tuple<T> {
        T left;
        T right;

        Tuple(T left, T right) {
            this.left = left;
            this.right = right;
        }
    }
    */

    public InterColumnGapExtractor() {
    }

    public void composeGaps(Page page) {
        //this.page = page;
        obstacles = new ArrayList<>();
        obstacles.addAll(page.getBlocks());
        obstacles.addAll(page.getImageBounds());
        //cursorTraces = page.getCursorTraces();
        //obstacles.addAll(page.getCursorTraces());
        Gap<List<CursorTrace>> gap = findRulings();
        if (null != gap)
            page.setGap(gap);
    }

    private Gap<List<CursorTrace>> findRulings() {
        if (obstacles == null && obstacles.isEmpty())
            return null;

        Rectangle2D boundary = getBBox();


        List<CursorTrace> leftCursorTraces = new ArrayList<>();
        List<CursorTrace> rightCursorTraces = new ArrayList<>();
        List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.addAll(obstacles);

        rectangles.sort(Comparator.comparing(Rectangle2D::getX));

        Double boundingTop = boundary.getY();
        Double boundingBottom = boundary.getY() + boundary.getHeight();
        Double boundingLeft = boundary.getX();
        Double boundingRight = boundary.getX() + boundary.getWidth();

        // add left bounding line
        leftCursorTraces.add(new CursorTrace(
                new Point2D.Double(boundingRight, boundingTop),
                new Point2D.Double(boundingRight, boundingBottom)
        ));

        // find all left lines
        for (Rectangle2D currentRect : rectangles) {
            //CursorTrace line = new CursorTrace(new Point2D.Double(rect.getX(), boundary.getY()), new Point2D.Double(rect.getX(), boundary.getY()+ boundary.getHeight()));
            Double x = currentRect.getX();
            Double currentTop = currentRect.getY();
            Double currentBottom = currentRect.getY() + currentRect.getHeight();

            Double y1 = boundingTop;
            Double y2 = boundingBottom;

            for (Rectangle2D obstacle : rectangles) {

                Double obstacleBottom = obstacle.getY() + obstacle.getHeight();
                Double obstacleLeft = obstacle.getX();
                Double obstacleTop = obstacle.getY();
                Double obstacleRight = obstacle.getX() + obstacle.getWidth();

                if (x > obstacleLeft && x < obstacleRight) {
                    if (obstacleBottom < currentTop) {
                        if (obstacleBottom > y1) y1 = obstacleBottom;
                    }
                    if (obstacleTop > currentBottom)
                        if (obstacleTop < y2) y2 = obstacleTop;
                }
            }

            CursorTrace line = new CursorTrace(new Point2D.Double(x, y1), new Point2D.Double(x, y2));
            if (!leftCursorTraces.contains(line)) {
                leftCursorTraces.add(line);
            }
        }

        rectangles.sort(Comparator.comparing(Rectangle2D::getX).reversed());

        // add right bounding line
        rightCursorTraces.add(new CursorTrace(
                new Point2D.Double(boundingLeft, boundingTop),
                new Point2D.Double(boundingLeft, boundingBottom)
        ));

        // find right lines
        for (Rectangle2D currentRect : rectangles) {
            //CursorTrace line = new CursorTrace(new Point2D.Double(rect.getX(), boundary.getY()), new Point2D.Double(rect.getX(), boundary.getY()+ boundary.getHeight()));
            Double x = currentRect.getX() + currentRect.getWidth();
            Double currentTop = currentRect.getY();
            Double currentBottom = currentRect.getY() + currentRect.getHeight();

            Double y1 = boundingTop;
            Double y2 = boundingBottom;

            for (Rectangle2D obstacle : rectangles) {

                Double obstacleBottom = obstacle.getY() + obstacle.getHeight();
                Double obstacleLeft = obstacle.getX();
                Double obstacleTop = obstacle.getY();
                Double obstacleRight = obstacle.getX() + obstacle.getWidth();

                if (x > obstacleLeft && x < obstacleRight) {
                    if (obstacleBottom < currentTop) {
                        if (obstacleBottom > y1) y1 = obstacleBottom;
                    }
                    if (obstacleTop > currentBottom)
                        if (obstacleTop < y2) y2 = obstacleTop;
                }
            }

            CursorTrace line = new CursorTrace(new Point2D.Double(x, y1), new Point2D.Double(x, y2));
            if (!rightCursorTraces.contains(line)) {
                rightCursorTraces.add(line);
            }
        }

        return new Gap<>(leftCursorTraces, rightCursorTraces);
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

    public static class Gap<T> {

        T left;
        T right;

        public Gap(T left, T right) {
            this.left = left;
            this.right = right;
        }

        public T getLeft() {
            return left;
        }

        public T getRight() {
            return right;
        }
    }
}
