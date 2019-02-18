package ru.icc.td.tabbypdf2.util;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.util.*;
import java.util.List;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.PDFToImage;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.imgcodecs.Imgcodecs;

import javax.imageio.ImageIO;

public class Utils {

    public static boolean within(double first, double second, double variance) {
        return second < first + variance && second > first - variance;
    }

    public static boolean overlap(double y1, double height1, double y2, double height2, double variance) {
        return within(y1, y2, variance) || (y2 <= y1 && y2 >= y1 - height1) || (y1 <= y2 && y1 >= y2 - height2);
    }

    public static boolean overlap(double y1, double height1, double y2, double height2) {
        return overlap(y1, height1, y2, height2, 0.1f);
    }

    private final static float EPSILON = 0.01f;
    protected static boolean useQuickSort = useCustomQuickSort();

    public static boolean feq(double f1, double f2) {
        return (Math.abs(f1 - f2) < EPSILON);
    }

    public static float round(double d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Double.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd.floatValue();
    }

    public static Rectangle bounds(Collection<? extends Shape> shapes) {
        if (shapes.isEmpty()) {
            throw new IllegalArgumentException("shapes can't be empty");
        }

        Iterator<? extends Shape> iter = shapes.iterator();
        Rectangle rv = new Rectangle();
        rv.setRect(iter.next().getBounds2D());

        while (iter.hasNext()) {
            Rectangle2D.union(iter.next().getBounds2D(), rv, rv);
        }

        return rv;

    }

    // range iterator
    public static List<Integer> range(final int begin, final int end) {
        return new AbstractList<Integer>() {
            @Override
            public Integer get(int index) {
                return begin + index;
            }

            @Override
            public int size() {
                return end - begin;
            }
        };
    }


    /* from apache.commons-lang */
    public static boolean isNumeric(final CharSequence cs) {
        if (cs == null || cs.length() == 0) {
            return false;
        }
        final int sz = cs.length();
        for (int i = 0; i < sz; i++) {
            if (!Character.isDigit(cs.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String join(String glue, String... s) {
        int k = s.length;
        if (k == 0) {
            return null;
        }
        StringBuilder out = new StringBuilder();
        out.append(s[0]);
        for (int x = 1; x < k; ++x) {
            out.append(glue).append(s[x]);
        }
        return out.toString();
    }

    public static <T> List<List<T>> transpose(List<List<T>> table) {
        List<List<T>> ret = new ArrayList<>();
        final int N = table.get(0).size();
        for (int i = 0; i < N; i++) {
            List<T> col = new ArrayList<>();
            for (List<T> row : table) {
                col.add(row.get(i));
            }
            ret.add(col);
        }
        return ret;
    }

    public static BufferedImage convertPageToImage(PDPage page, int dpi, ImageType imageType) {
        PDDocument document;
        try {
            document = new PDDocument();
            document.addPage(page);
            PDFRenderer renderer = new PDFRenderer(document);
            BufferedImage img = renderer.renderImageWithDPI(0, dpi, imageType);
            return img;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean useCustomQuickSort() {
        // taken from PDFBOX:

        // check if we need to use the custom quicksort algorithm as a
        // workaround to the transitivity issue of TextPositionComparator:
        // https://issues.apache.org/jira/browse/PDFBOX-1512

        String numberybits = System.getProperty("java.version").split(
                "-")[0]; // some Java version strings are 9-internal, which is dumb.
        String[] versionComponents = numberybits.split(
                "\\.");
        int javaMajorVersion;
        int javaMinorVersion;
        if (versionComponents.length >= 2) {
            javaMajorVersion = Integer.parseInt(versionComponents[0]);
            javaMinorVersion = Integer.parseInt(versionComponents[1]);
        } else {
            javaMajorVersion = 1;
            javaMinorVersion = Integer.parseInt(versionComponents[0]);
        }
        boolean is16orLess = javaMajorVersion == 1 && javaMinorVersion <= 6;
        String useLegacySort = System.getProperty("java.util.Arrays.useLegacyMergeSort");
        return !is16orLess || (useLegacySort != null && useLegacySort.equals("true"));
    }


    public static List<Integer> parsePagesOption(String pagesSpec) throws Exception {
        if (pagesSpec.equals("all")) {
            return null;
        }

        List<Integer> rv = new ArrayList<>();

        String[] ranges = pagesSpec.split(",");
        for (int i = 0; i < ranges.length; i++) {
            String[] r = ranges[i].split("-");
            if (r.length == 0 || !Utils.isNumeric(r[0]) || r.length > 1 && !Utils.isNumeric(r[1])) {
                throw new Exception("Syntax error in page range specification");
            }

            if (r.length < 2) {
                rv.add(Integer.parseInt(r[0]));
            } else {
                int t = Integer.parseInt(r[0]);
                int f = Integer.parseInt(r[1]);
                if (t > f) {
                    throw new Exception("Syntax error in page range specification");
                }
                rv.addAll(Utils.range(t, f + 1));
            }
        }

        Collections.sort(rv);
        return rv;
    }

    public static void snapPoints(List<? extends Line2D.Float> rulings, float xThreshold, float yThreshold) {

        // collect points and keep a Line -> p1,p2 map
        Map<Line2D.Float, Point2D[]> linesToPoints = new HashMap<>();
        List<Point2D> points = new ArrayList<>();
        for (Line2D.Float r : rulings) {
            Point2D p1 = r.getP1();
            Point2D p2 = r.getP2();
            linesToPoints.put(r, new Point2D[]{p1, p2});
            points.add(p1);
            points.add(p2);
        }

        // snap by X
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
        // ---

        // snap by Y
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
        // ---

        // finally, modify lines
        for (Map.Entry<Line2D.Float, Point2D[]> ltp : linesToPoints.entrySet()) {
            Point2D[] p = ltp.getValue();
            ltp.getKey().setLine(p[0], p[1]);
        }
    }

    public static BufferedImage pageConvertToImage(PDPage page, int dpi, ImageType imageType) throws IOException {
        try (PDDocument document = new PDDocument()) {
            document.addPage(page);
            PDFRenderer renderer = new PDFRenderer(document);
            document.close();
            return renderer.renderImageWithDPI(0, dpi, imageType);
        }
    }

    public static final class CohenSutherlandClipping
    {
        private double xMin;
        private double yMin;
        private double xMax;
        private double yMax;

        /**
         * Creates a Cohen Sutherland clipper with clip rect (0, 0, 0, 0).
         */
        public CohenSutherlandClipping() {
        }

        /**
         * Creates a Cohen Sutherland clipper with the given clip rectangle.
         * @param clip the clip rectangle to use
         */
        public CohenSutherlandClipping(Rectangle2D clip) {
            setClip(clip);
        }

        /**
         * Sets the clip rectangle.
         * @param clip the clip rectangle
         */
        public void setClip(Rectangle2D clip) {
            xMin = clip.getX();
            xMax = xMin + clip.getWidth();
            yMin = clip.getY();
            yMax = yMin + clip.getHeight();
        }

        private static final int INSIDE = 0;
        private static final int LEFT   = 1;
        private static final int RIGHT  = 2;
        private static final int BOTTOM = 4;
        private static final int TOP    = 8;

        private final int regionCode(double x, double y) {
            int code = x < xMin
                    ? LEFT
                    : x > xMax
                    ? RIGHT
                    : INSIDE;
            if (y < yMin) code |= BOTTOM;
            else if (y > yMax) code |= TOP;
            return code;
        }

        /**
         * Clips a given line against the clip rectangle.
         * The modification (if needed) is done in place.
         * @param line the line to clip
         * @return true if line is clipped, false if line is
         * totally outside the clip rect.
         */
        public boolean clip(Line2D.Float line) {

            double p1x = line.getX1();
            double p1y = line.getY1();
            double p2x = line.getX2();
            double p2y = line.getY2();

            double qx = 0d;
            double qy = 0d;

            boolean vertical = p1x == p2x;

            double slope = vertical
                    ? 0d
                    : (p2y-p1y)/(p2x-p1x);

            int c1 = regionCode(p1x, p1y);
            int c2 = regionCode(p2x, p2y);

            while (c1 != INSIDE || c2 != INSIDE) {

                if ((c1 & c2) != INSIDE)
                    return false;

                int c = c1 == INSIDE ? c2 : c1;

                if ((c & LEFT) != INSIDE) {
                    qx = xMin;
                    qy = (Utils.feq(qx, p1x) ? 0 : qx-p1x)*slope + p1y;
                }
                else if ((c & RIGHT) != INSIDE) {
                    qx = xMax;
                    qy = (Utils.feq(qx, p1x) ? 0 : qx-p1x)*slope + p1y;
                }
                else if ((c & BOTTOM) != INSIDE) {
                    qy = yMin;
                    qx = vertical
                            ? p1x
                            : (Utils.feq(qy, p1y) ? 0 : qy-p1y)/slope + p1x;
                }
                else if ((c & TOP) != INSIDE) {
                    qy = yMax;
                    qx = vertical
                            ? p1x
                            : (Utils.feq(qy, p1y) ? 0 : qy-p1y)/slope + p1x;
                }

                if (c == c1) {
                    p1x = qx;
                    p1y = qy;
                    c1  = regionCode(p1x, p1y);
                }
                else {
                    p2x = qx;
                    p2y = qy;
                    c2 = regionCode(p2x, p2y);
                }
            }
            line.setLine(p1x, p1y, p2x, p2y);
            return true;
        }
    }

    public static File createOutputFile(File file, String subDirName, Path debugDirectoryPath, String fileExt) {
        // Make the specified output directory
        Path outputDirectoryPath = debugDirectoryPath;

        if (null != subDirName)
            outputDirectoryPath = debugDirectoryPath.resolve(subDirName);

        outputDirectoryPath.toFile().mkdirs();

        String fileName = FilenameUtils.removeExtension(file.getName());

        return new File(String.format("%s/%s.%s", outputDirectoryPath, fileName, fileExt));
    }

    public static File createOutputFile(File file, String subDirName, Path debugDirectoryPath, String suffix, String fileExt) {
        // Make the specified output directory
        Path outputDirectoryPath = debugDirectoryPath;

        if (null != subDirName)
            outputDirectoryPath = debugDirectoryPath.resolve(subDirName);

        outputDirectoryPath.toFile().mkdirs();

        String fileName = FilenameUtils.removeExtension(file.getName()) + suffix;

        return new File(String.format("%s/%s.%s", outputDirectoryPath, fileName, fileExt));
    }

    // Convert image to Mat
    public static Mat matify(BufferedImage im) {
        // Convert INT to BYTE
        //im = new BufferedImage(im.getWidth(), im.getHeight(),BufferedImage.TYPE_3BYTE_BGR);
        // Convert bufferedimage to byte array
        byte[] pixels = ((DataBufferByte) im.getRaster().getDataBuffer())
                .getData();

        // Create a Matrix the same size of image
        Mat image = new Mat(im.getHeight(), im.getWidth(), CvType.CV_8UC3);
        // Fill Matrix with image values
        image.put(0, 0, pixels);

        return image;

    }

    public static BufferedImage mat2BufferedImage(Mat matrix) throws IOException {
        MatOfByte mob=new MatOfByte();
        Imgcodecs.imencode(".jpg", matrix, mob);
        return ImageIO.read(new ByteArrayInputStream(mob.toArray()));
    }

}
