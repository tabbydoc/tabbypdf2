package ru.icc.td.tabbypdf2.read;

import org.apache.pdfbox.contentstream.PDFGraphicsStreamEngine;
import org.apache.pdfbox.cos.COSName;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.graphics.image.PDImage;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ru.icc.td.tabbypdf2.model.CursorTrace;
import ru.icc.td.tabbypdf2.model.Page;

public final class PDFGraphicsExtractor {

    private final PDDocument pdDocument;          // A source untagged PDF document that needs to be processed
    private final List<CursorTrace> cursorTraces; // Cursor traces extracted from the PDF document
    private final List<Rectangle2D> imageBounds;  // Image bounding boxes extracted from the PDF document

    public PDFGraphicsExtractor(PDDocument pdDocument) throws IOException {
        this.pdDocument = pdDocument;
        cursorTraces = new ArrayList<>(1000);
        imageBounds = new ArrayList<>(10);
    }

    public void readTo(final int pageIndex, Page page) {
        if (null == page) {
            throw new IllegalArgumentException("The page cannot be null");
        } else {
            try {
                PDPage pdPage = pdDocument.getPage(pageIndex);
                new InnerStreamEngine(pdPage).run();
                page.addCursorTraces(cursorTraces);
                page.addImageBounds(imageBounds);
            } finally {
                clearAll();
            }
        }
    }

    private void clearAll() {
        cursorTraces.clear();
        imageBounds.clear();
    }

    private final class InnerStreamEngine extends PDFGraphicsStreamEngine {
        private float x, y;

        private InnerStreamEngine(PDPage page) {
            super(page);
        }

        private void run() {
            try {
                processPage(getPage());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void appendRectangle(Point2D p0, Point2D p1, Point2D p2, Point2D p3) throws IOException {

            if (canAddCursorTrace(p0, p1))
                cursorTraces.add(new CursorTrace(p0, p1));

            if (canAddCursorTrace(p1, p2))
                cursorTraces.add(new CursorTrace(p1, p2));

            if (canAddCursorTrace(p2, p3))
                cursorTraces.add(new CursorTrace(p2, p3));

            if (canAddCursorTrace(p3, p0))
                cursorTraces.add(new CursorTrace(p3, p0));
        }

        @Override
        public void drawImage(PDImage pdImage) throws IOException {
            AffineTransform at = getGraphicsState().getCurrentTransformationMatrix().createAffineTransform();
            at.scale(1d, -1d);
            at.translate(0d, -1d);

            Rectangle2D imageBBox = at.createTransformedShape(new Rectangle2D.Double(0d, 0d, 1d, 1d)).getBounds2D();
            imageBounds.add(imageBBox);
        }

        @Override
        public void clip(int windingRule) throws IOException {
        }

        @Override
        public void moveTo(float x, float y) throws IOException {
            this.x = x;
            this.y = y;
        }

        @Override
        public void lineTo(float x, float y) throws IOException {

            if (canAddCursorTrace(this.x, this.y, x, y))
                cursorTraces.add(new CursorTrace(this.x, this.y, x, y));

            this.x = x;
            this.y = y;
        }

        @Override
        public void curveTo(float x1, float y1, float x2, float y2, float x3, float y3) throws IOException {
        }

        @Override
        public Point2D getCurrentPoint() throws IOException {
            return new Point2D.Float(x, y);
        }

        @Override
        public void closePath() throws IOException {
        }

        @Override
        public void endPath() throws IOException {
        }

        @Override
        public void strokePath() throws IOException {
        }

        @Override
        public void fillPath(int windingRule) throws IOException {
        }

        @Override
        public void fillAndStrokePath(int windingRule) throws IOException {
        }

        @Override
        public void shadingFill(COSName shadingName) throws IOException {
        }

        private final static double MIN_CURSOR_TRACE_LONG = 10.0; 

        // Check if these coordinates can be used to register a new cursor trace,
        // assuming that the cursor trace is oriented and longer then <code>MIN_CURSOR_TRACE_LONG</code>
        private boolean canAddCursorTrace(double x1, double y1, double x2, double y2) {
            if (x1 == x2)
                return Math.abs(y1 - y2) > MIN_CURSOR_TRACE_LONG;
            else if (y1 == y2)
                return Math.abs(x1 - x2) > MIN_CURSOR_TRACE_LONG;
            else
                return false;
        }

        // Check if these points can be used to register a new cursor trace
        private boolean canAddCursorTrace(Point2D p1, Point2D p2) {
            return canAddCursorTrace(p1.getX(), p1.getY(), p2.getX(), p2.getY());
        }
    }
}