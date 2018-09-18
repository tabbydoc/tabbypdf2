package ru.icc.td.tabbypdf2.comp;

import ru.icc.td.tabbypdf2.model.Block;
import ru.icc.td.tabbypdf2.model.Gap;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Ruling;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PageLayoutAlgorithm {

    //private Page page;
    //private Rectangle2D.Float boundary;
    private List<Rectangle2D> obstacles;
    private List<Ruling> rulings;
    private Tuple<List<Ruling>> linesTuple;

    private final Comparator<Rectangle2D.Float> RECTANGLE_COMPARATOR = Comparator
            .comparing(Rectangle2D.Float::getY)
            .reversed()
            .thenComparing(Rectangle2D.Float::getX);

    private class Tuple<T> {
        T left;
        T right;

        Tuple(T left, T right) {
            this.left = left;
            this.right = right;
        }
    }

    public PageLayoutAlgorithm() {
    }

    public void composeGaps(Page page) {
        //this.page = page;
        obstacles = new ArrayList<>();
        obstacles.addAll(page.getBlocks());
        obstacles.addAll(page.getImageBounds());
        rulings = page.getRulings();
        //obstacles.addAll(page.getRulings());
        page.setGap(findRulings());
    }

    private Gap<List<Ruling>> findRulings() {

        Rectangle2D.Double boundary = getBoundingRectangle();
        List<Ruling> leftRulings = new ArrayList<>();
        List<Ruling> rightRulings = new ArrayList<>();
        List<Rectangle2D> rectangles = new ArrayList<>();
        rectangles.addAll(obstacles);

        rectangles.sort(Comparator.comparing(Rectangle2D::getX));

        Double boundingTop = boundary.getY();
        Double boundingBottom = boundary.getY() + boundary.getHeight();
        Double boundingLeft = boundary.getX();
        Double boundingRight = boundary.getX() + boundary.getWidth();

        // add left bounding line
        leftRulings.add(new Ruling(
                new Point2D.Double(boundingRight, boundingTop),
                new Point2D.Double(boundingRight, boundingBottom)
        ));

        // find all left lines
        for (Rectangle2D currentRect:rectangles) {
            //Ruling line = new Ruling(new Point2D.Double(rect.getX(), boundary.getY()), new Point2D.Double(rect.getX(), boundary.getY()+ boundary.getHeight()));
            Double x = currentRect.getX();
            Double currentTop = currentRect.getY();
            Double currentBottom = currentRect.getY() + currentRect.getHeight();

            Double y1 = boundingTop;
            Double y2 = boundingBottom;

            for (Rectangle2D obstacle:rectangles) {

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

            Ruling line = new Ruling(new Point2D.Double(x, y1), new Point2D.Double(x, y2) );
            if (!leftRulings.contains(line)) {
                leftRulings.add(line);
            }
        }

        rectangles.sort(Comparator.comparing(Rectangle2D::getX).reversed());

        // add right bounding line
        rightRulings.add(new Ruling(
                new Point2D.Double(boundingLeft, boundingTop),
                new Point2D.Double(boundingLeft, boundingBottom)
        ));

        // find right lines
        for (Rectangle2D currentRect:rectangles) {
            //Ruling line = new Ruling(new Point2D.Double(rect.getX(), boundary.getY()), new Point2D.Double(rect.getX(), boundary.getY()+ boundary.getHeight()));
            Double x = currentRect.getX() + currentRect.getWidth();
            Double currentTop = currentRect.getY();
            Double currentBottom = currentRect.getY() + currentRect.getHeight();

            Double y1 = boundingTop;
            Double y2 = boundingBottom;

            for (Rectangle2D obstacle:rectangles) {

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

            Ruling line = new Ruling(new Point2D.Double(x, y1), new Point2D.Double(x, y2) );
            if (!rightRulings.contains(line)) {
                rightRulings.add(line);
            }
        }

        return new Gap<>(leftRulings, rightRulings);
    }

    private Rectangle2D.Double getBoundingRectangle() {
        if (obstacles!=null && obstacles.size() > 0) {
            Rectangle2D rectangle = obstacles.get(0);

            double left = rectangle.getX();
            double right = rectangle.getX() + rectangle.getWidth();
            double top = rectangle.getY();
            double bottom = rectangle.getY() + rectangle.getHeight();

            for (Rectangle2D r:obstacles) {
                if(r.getX() < left) left = r.getX();
                if(r.getX() + r.getWidth() > right) right = r.getX() + r.getWidth();
                if(r.getY() < top) top = r.getY();
                if(r.getY() + r.getHeight() > bottom) bottom = r.getY() + r.getHeight();
            }

            return new Rectangle2D.Double( left - 10, top, right - left + 20, bottom - top);
        }
        return null;
    }

    /*private List<Rectangle2D.Float> getPreparedObstacles() {
        List<Rectangle2D.Float> result = new ArrayList<>();
        result.addAll(obstacles);
        result.sort(RECTANGLE_COMPARATOR);
        return result;
    }*/

}
