package ru.icc.td.tabbypdf2.util;

import ru.icc.td.tabbypdf2.model.Ruling;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;

public class Utils {

    //public static final Logger log = LogManager.getLogger(SDSTableExtractor.class.getName());

    private final static float EPSILON = 0.01f;
    public final static float POINT_SNAP_DISTANCE_THRESHOLD = 8f;

    public static BufferedImage convertPageToImage(PDPage page, int dpi, ImageType imageType) {
        try (PDDocument document = new PDDocument()) {
            document.addPage(page);
            PDFRenderer renderer = new PDFRenderer(document);
            document.close();
            return renderer.renderImageWithDPI(0, dpi, imageType);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static boolean within(double first, double second, double variance) {
        return second < first + variance && second > first - variance;
    }

    public static float round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static boolean feq(double f1, double f2) {
        return (Math.abs(f1 - f2) < EPSILON);
    }


    public static List<Ruling> collapseOrientedRulings(List<Ruling> lines, int expandAmount) {
        ArrayList<Ruling> rv = new ArrayList<>();
        Collections.sort(lines, new Comparator<Ruling>() {
            @Override
            public int compare(Ruling a, Ruling b) {
                final float diff = a.getPosition() - b.getPosition();
                return java.lang.Float.compare(diff == 0 ? a.getStart() - b.getStart() : diff, 0f);
            }
        });

        for (Ruling next_line : lines) {
            Ruling last = rv.isEmpty() ? null : rv.get(rv.size() - 1);
            if (last != null && Utils.feq(next_line.getPosition(), last.getPosition()) &&
                    last.nearlyIntersects(next_line, expandAmount)) {
                final float lastStart = last.getStart();
                final float lastEnd = last.getEnd();

                final boolean lastFlipped = lastStart > lastEnd;
                final boolean nextFlipped = next_line.getStart() > next_line.getEnd();

                boolean differentDirections = nextFlipped != lastFlipped;
                float nextS = differentDirections ? next_line.getEnd() : next_line.getStart();
                float nextE = differentDirections ? next_line.getStart() : next_line.getEnd();

                final float newStart = lastFlipped ? Math.max(nextS, lastStart) : Math.min(nextS, lastStart);
                final float newEnd  = lastFlipped ? Math.min(nextE, lastEnd) : Math.max(nextE, lastEnd);
                last.setStartEnd(newStart, newEnd);
                assert !last.oblique();
            }
            else if (next_line.length() == 0) {
                continue;
            }
            else {
                rv.add(next_line);
            }
        }
        return rv;
    }


    public static void snapPoints(List<? extends Line2D.Float> rulings, float xThreshold, float yThreshold) {

        Map<Line2D.Float, Point2D[]> linesToPoints = new HashMap<>();
        List<Point2D> points = new ArrayList<>();
        for (Line2D.Float r : rulings) {
            Point2D p1 = r.getP1();
            Point2D p2 = r.getP2();
            linesToPoints.put(r, new Point2D[]{p1, p2});
            points.add(p1);
            points.add(p2);
        }

        Collections.sort(points, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D arg0, Point2D arg1) {
                return java.lang.Double.compare(arg0.getX(), arg1.getX());
            }
        });

        List<List<Point2D>> groupedPoints = new ArrayList<>();
        groupedPoints.add(new ArrayList<>(Arrays.asList(new Point2D[]{points.get(0)})));

        for (Point2D p : points.subList(1, points.size() - 1)) {
            List<Point2D> last = groupedPoints.get(groupedPoints.size() - 1);
            if (Math.abs(p.getX() - last.get(0).getX()) < xThreshold) {
                groupedPoints.get(groupedPoints.size() - 1).add(p);
            } else {
                groupedPoints.add(new ArrayList<>(Arrays.asList(new Point2D[]{p})));
            }
        }

        for (List<Point2D> group : groupedPoints) {
            float avgLoc = 0;
            for (Point2D p : group) {
                avgLoc += p.getX();
            }
            avgLoc /= group.size();
            for (Point2D p : group) {
                p.setLocation(avgLoc, p.getY());
            }
        }

        Collections.sort(points, new Comparator<Point2D>() {
            @Override
            public int compare(Point2D arg0, Point2D arg1) {
                return java.lang.Double.compare(arg0.getY(), arg1.getY());
            }
        });

        groupedPoints = new ArrayList<>();
        groupedPoints.add(new ArrayList<>(Arrays.asList(new Point2D[]{points.get(0)})));

        for (Point2D p : points.subList(1, points.size() - 1)) {
            List<Point2D> last = groupedPoints.get(groupedPoints.size() - 1);
            if (Math.abs(p.getY() - last.get(0).getY()) < yThreshold) {
                groupedPoints.get(groupedPoints.size() - 1).add(p);
            } else {
                groupedPoints.add(new ArrayList<>(Arrays.asList(new Point2D[]{p})));
            }
        }

        for (List<Point2D> group : groupedPoints) {
            float avgLoc = 0;
            for (Point2D p : group) {
                avgLoc += p.getY();
            }
            avgLoc /= group.size();
            for (Point2D p : group) {
                p.setLocation(p.getX(), avgLoc);
            }
        }

        for (Map.Entry<Line2D.Float, Point2D[]> ltp : linesToPoints.entrySet()) {
            Point2D[] p = ltp.getValue();
            ltp.getKey().setLine(p[0], p[1]);
        }
    }

}
