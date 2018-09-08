package ru.icc.td.tabbypdf2.debug;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import java.awt.Color;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.io.IOException;

final class ContentDrawer {
    private final PDDocument pdDocument;
    private PDPageContentStream contentStream;

    // Content stream settings by default
    private static final Color STROKE_COLOR;
    private static final Color FILL_COLOR;
    private static final float lINE_WIDTH;
    private static final PDFont FONT;
    private static final int FONT_SIZE;

    static {
        STROKE_COLOR = Color.BLACK;
        FILL_COLOR = Color.BLACK;
        lINE_WIDTH = 0.25f;
        FONT = PDType1Font.HELVETICA;
        FONT_SIZE = 5;
    }

    // Content stream settings
    private Color strokeColor;
    private Color fillColor;
    private float lineWidth;

    {
        strokeColor = STROKE_COLOR;
        fillColor = FILL_COLOR;
        lineWidth = lINE_WIDTH;
    }

    ContentDrawer(PDDocument pdDocument) {
        this.pdDocument = pdDocument;
    }

    void startPage(int pageIndex) throws IOException {
        PDPage page = pdDocument.getPage(pageIndex);
        contentStream = new PDPageContentStream(pdDocument, page, PDPageContentStream.AppendMode.APPEND, true, true);
        setStyle(strokeColor, fillColor, lineWidth);
        contentStream.setFont(FONT, FONT_SIZE);
    }

    void endPage() throws IOException {
        contentStream.closeAndStroke();
        contentStream.close();
    }

    ContentDrawer setStrokeColor(Color color) {
        assert (null != color);
        strokeColor = color;
        return this;
    }

    ContentDrawer setFillColor(Color color) {
        assert (null != color);
        fillColor = color;
        return this;
    }

    ContentDrawer setLineWidth(float width) {
        assert (width > 0f);
        lineWidth = width;
        return this;
    }

    void setStyle(Color strokeColor, Color fillColor, float lineWidth) throws IOException {
        contentStream.setStrokingColor(strokeColor);
        contentStream.setNonStrokingColor(fillColor);
        contentStream.setLineWidth(lineWidth);
    }

    void strokeRectangle(Rectangle2D.Float rect) throws IOException {
        contentStream.addRect(rect.x, rect.y, rect.width, rect.height);
        contentStream.stroke();
    }

    void fillRectangle(Rectangle2D.Float rect) throws IOException {
        contentStream.addRect(rect.x, rect.y, rect.width, rect.height);
        contentStream.fill();
    }

    void strokeLine(Line2D.Float line) throws IOException {
        contentStream.moveTo(line.x1, line.y1);
        contentStream.lineTo(line.x2, line.y2);
        contentStream.stroke();
        contentStream.moveTo(line.x1, line.y1);
    }

    void showText(String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }
}