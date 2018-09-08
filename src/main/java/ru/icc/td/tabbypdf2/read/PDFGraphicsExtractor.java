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
import java.util.Arrays;
import java.util.List;

import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Ruling;

public class PDFGraphicsExtractor {

    private final PDDocument pdDocument; // A source untagged PDF document that needs to be processed
    private final List<Ruling> rulings;  // Rulings extracted from the PDF document
    private final List<Rectangle2D> imageRegions;

    public PDFGraphicsExtractor(PDDocument pdDocument) throws IOException {
        this.pdDocument = pdDocument;
        rulings = new ArrayList<>(1000);
        imageRegions = new ArrayList<>();
    }

    public void readTo(final int pageIndex, Page page) {
        if (null == page) {
            throw new IllegalArgumentException("The page cannot be null");
        } else {
            try {
                PDPage pdPage = pdDocument.getPage(pageIndex);
                new RulingAndImageStreamEngine(pdPage).run();
                page.addRulings(rulings);
            } finally {
                clearAll();
            }
        }
    }

    private void clearAll() {
        rulings.clear();
        imageRegions.clear();
    }

    private class RulingAndImageStreamEngine extends PDFGraphicsStreamEngine {
        private float x, y;

        private RulingAndImageStreamEngine(PDPage page) {
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
            rulings.addAll(Arrays.asList(
                    new Ruling(p0, p1),
                    new Ruling(p1, p2),
                    new Ruling(p2, p3),
                    new Ruling(p3, p0)
            ));
        }

        @Override
        public void drawImage(PDImage pdImage) throws IOException {
            AffineTransform at = getGraphicsState().getCurrentTransformationMatrix().createAffineTransform();
            at.scale(1d, -1d);
            at.translate(0d, -1d);

            Rectangle2D imageBounds = at.createTransformedShape(new Rectangle2D.Double(0d, 0d, 1d, 1d)).getBounds2D();
            imageRegions.add(imageBounds);
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
            rulings.add(new Ruling(this.x, this.y, x, y));
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
    }
}