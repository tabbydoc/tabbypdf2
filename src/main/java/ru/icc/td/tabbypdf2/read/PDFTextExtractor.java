package ru.icc.td.tabbypdf2.read;

import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.contentstream.operator.color.*;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDFontDescriptor;
import org.apache.pdfbox.pdmodel.graphics.color.PDColor;
import org.apache.pdfbox.pdmodel.graphics.state.RenderingMode;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.TextPosition;
import ru.icc.td.tabbypdf2.model.CharPosition;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.Color;
import java.awt.Font;
import java.awt.geom.Rectangle2D;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class PDFTextExtractor {

    private final PDDocument pdDocument;            // A source untagged PDF document needs to be processed
    private final List<CharPosition> charPositions; // Char Positions extracted from the PDF document

    public PDFTextExtractor(PDDocument pdDocument) throws IOException {
        this.pdDocument = pdDocument;
        charPositions = new ArrayList<>(5000);
    }

    private final InnerTextStripper innerTextStripper = new InnerTextStripper();

    public void readTo(final int pageIndex, Page page) {
        if (null == page) {
            throw new IllegalArgumentException("The page cannot be null");
        }
        else {
            try {
                innerTextStripper.stripPage(pageIndex);
                page.addCharPositions(charPositions);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            finally {
                innerTextStripper.clearAll();
            }
        }
    }

    private static final char[] WHITESPACES;
    private static final char[] BULLETS;

    static {
        BULLETS = new char[]{
                '\u2022', // bullet
                '\u2023', // triangular bullet
                '\u25E6', // white bullet
                '\u2043', // hyphen bullet
                '\u204C', // black leftwards bullet
                '\u204D', // black rightwards bullet
                '\u2219', // bullet operator
                '\u00B7', // middle dot
                '\u2024', // one dot leader
                '\u25D8', // inverse bullet
                '\u220E', // end of proof
                '\u25B8', // black right-pointing small triangle
                '\u2027', // hyphenation point
                '\u2043', // hyphen bullet
                '\u25A0', // black square
                '\u25A1', // white square
                '\u25A2', // white square with rounded corners
                '\u25AA', // black small square
                '\u25AB', // white small square
                '\u25CB', // white circle
                '\u25CC', // dotted circle
                '\u25CD', // circle with vertical fill
                '\u25CF', // black circle
                '\u25FB', // white medium square
                '\u25FC', // black medium square
                '\u25FD', // white medium small square
                '\u25FE', // black medium small square
                '\u25CA', // lozenge
                '\u2311', // square lozenge
                '\u28EB', // black lozenge
        };
    }

    static {
        WHITESPACES = new char[]{
                '\u0020', //  space
                '\u00A0', //  no-break space
                '\u0009', //  character tabulation
                '\n',     //  line feed
                '\u000B', //  line tabulation
                '\u000C', //  form feed
                '\r',     //  carriage return
                '\u0085', //  next line
                '\u1680', //  ogham space mark
                '\u2000', //  en quad
                '\u2001', //  em quad
                '\u2002', //  en space
                '\u2003', //  em space
                '\u2004', //  three-per-em space
                '\u2005', //  four-per-em space
                '\u2006', //  six-per-em space
                '\u2007', //  figure space
                '\u2008', //  punctuation space
                '\u2009', //  thin space
                '\u200A', //  hair space
                '\u2028', //  line separator
                '\u2029', //  paragraph separator
                '\u202F', //  narrow no-break space
                '\u205F', //  medium mathematical space
                '\u3000', //  ideographic space
                '\u180E', //  mongolian vowel separator
                '\u200B', //  zero width space
                '\u200C', //  zero width non-joiner
                '\u200D', //  zero width joiner
                '\u2060', //  word joiner
                '\uFEFF'  //  zero width non-breaking
        };
    }

    private static final FontNames fontNames = FontNames.getInstance();

    private final class InnerTextStripper extends PDFTextStripper {

        private int chunkCount; // An index of an original chunk in its PDF document

        // Settings
        {
            addOperator(new SetStrokingColor());
            addOperator(new SetStrokingColorN());
            addOperator(new SetStrokingColorSpace());
            addOperator(new SetStrokingDeviceCMYKColor());
            addOperator(new SetStrokingDeviceRGBColor());
            addOperator(new SetStrokingDeviceGrayColor());
            addOperator(new SetNonStrokingColor());
            addOperator(new SetNonStrokingColorN());
            addOperator(new SetNonStrokingColorSpace());
            addOperator(new SetNonStrokingDeviceCMYKColor());
            addOperator(new SetNonStrokingDeviceRGBColor());
            addOperator(new SetNonStrokingDeviceGrayColor());
        }

        private InnerTextStripper() throws IOException {
        }

        private void stripPage(int pageIndex) throws IOException {
            PDPage page = pdDocument.getPage(pageIndex);

            chunkCount = -1; // Each page starts the count of chunk IDs with 0
            pageIndex += 1;  // PDFBox page numbers are 1-based
            setStartPage(pageIndex);
            setEndPage(pageIndex);
            Writer dummy = new OutputStreamWriter(new ByteArrayOutputStream());
            super.writeText(pdDocument, dummy);
        }

        private void clearAll() {
            charPositions.clear();

            renderingMode.clear();
            strokingColor.clear();
            nonStrokingColor.clear();
        }

        private final Map<TextPosition, RenderingMode> renderingMode = new HashMap<>();
        private final Map<TextPosition, PDColor> strokingColor = new HashMap<>();
        private final Map<TextPosition, PDColor> nonStrokingColor = new HashMap<>();

        @Override
        protected void processTextPosition(TextPosition text) {
            renderingMode.put(text, getGraphicsState().getTextState().getRenderingMode());
            strokingColor.put(text, getGraphicsState().getStrokingColor());
            nonStrokingColor.put(text, getGraphicsState().getNonStrokingColor());

            super.processTextPosition(text);
        }

        @Override
        protected void startPage(PDPage page) throws IOException {
            super.startPage(page);
        }

        @Override
        protected void endPage(PDPage page) throws IOException {
            super.endPage(page);
        }

        @Override
        protected void writeLineSeparator() throws IOException {
            super.writeLineSeparator();
        }

        private Color getColor(TextPosition textPos) throws IOException {
            RenderingMode rm = renderingMode.get(textPos);
            if (rm == RenderingMode.FILL || rm == RenderingMode.NEITHER) {
                PDColor pdColor = nonStrokingColor.get(textPos);
                return new Color(pdColor.toRGB());
            }
            if (rm == RenderingMode.STROKE) {
                PDColor pdColor = strokingColor.get(textPos);
                return new Color(pdColor.toRGB());
            }
            return Color.BLACK;
        }

        private Font getFont(TextPosition textPosition) {
            if (textPosition == null)
                return null;

            PDFont font = textPosition.getFont();
            if (font == null)
                return null;

            PDFontDescriptor fontDescriptor = font.getFontDescriptor();
            if (fontDescriptor == null)
                return null;

            String fontName = fontDescriptor.getFontName();
            if (fontName == null)
                return null;

            // Seek a normalized font name
            String normFontName = fontNames.normFontName(fontName);
            if (normFontName == null)
                normFontName = fontName;

            // Is the font italic?
            boolean isFontItalic;
            if (fontDescriptor.isItalic()) {
                isFontItalic = true;
            } else if (fontName.toLowerCase().contains("oblique")) {
                isFontItalic = true;
            } else isFontItalic = fontName.toLowerCase().contains("italic");

            // Is the font bold?
            boolean isFontBold;
            if (fontDescriptor.isForceBold()) {
                isFontBold = true;
            } else if (fontDescriptor.getFontWeight() >= 700) {
                isFontBold = true;
            } else if (fontDescriptor.getFontName().toLowerCase().contains("bold")) {
                isFontBold = true;
            } else {
                RenderingMode rm = renderingMode.get(textPosition);
                isFontBold = (rm == RenderingMode.FILL_STROKE);
            }

            // Get the font size in pt
            int fontSize = (int) textPosition.getFontSizeInPt();

            // Create and return new font
            if (isFontBold) {
                if (isFontItalic) {
                    return new Font(normFontName, Font.BOLD + Font.ITALIC, fontSize);
                } else {
                    return new Font(normFontName, Font.BOLD, fontSize);
                }
            } else {
                if (isFontItalic) {
                    return new Font(normFontName, Font.ITALIC, fontSize);
                } else {
                    return new Font(normFontName, Font.PLAIN, fontSize);
                }
            }
        }

        private boolean isWhitespace(String text) {
            for (char c : text.toCharArray())
                for (char wordSeparator : WHITESPACES)
                    if (c == wordSeparator)
                        return true;

            return false;
        }

        private boolean isBullet(String text){
            for (char c : text.toCharArray())
                for (char wordSeparator : BULLETS)
                    if (c == wordSeparator)
                        return true;

            return false;
        }

        @Override
        protected void writeString(String string, List<TextPosition> textPositions) throws IOException {
            // The invocation of this method signals a new text chunk in the PDF document.
            // Therefore we increment <code>chunkCount</code> variable
            // to set the corresponding chunk ID to the extracted char positions of this text chunk.
            chunkCount++;

            if (null == textPositions || textPositions.isEmpty())
                return;

            // Check if the string contains printable characters
            if (StringUtils.isBlank(string) || string.replaceAll("\\P{Print}", "").isEmpty())
                return;

            // Build char positions from text positions
            for (TextPosition textPos : textPositions) {
                String unicode = textPos.getUnicode();
                if (unicode == null || StringUtils.isBlank(unicode)) continue;
                if (isWhitespace(unicode) || isBullet(unicode)) continue;

                // Check if the text position is not rotated
                if (textPos.getDir() != 0) {
                    System.err.println("WARNING: a directed text position was ignored");
                    continue;
                }

                Character c = unicode.charAt(0);

                // Text position coordinates
                final float x = textPos.getX();
                final float y = textPos.getPageHeight() - textPos.getY();
                final float w = textPos.getWidth();
                final float h = textPos.getHeight();

                Rectangle2D bbox = new Rectangle2D.Float(x, y, w, h);

                float spaceWidth = textPos.getWidthOfSpace();

                // Check if the font of the text position is not null
                Font font = getFont(textPos);
                if (null == font) {
                    System.err.println("WARNING: a text position whose font is null was ignored");
                    continue;
                }

                Color color = getColor(textPos);

                CharPosition charPosition = new CharPosition(chunkCount, c, bbox, spaceWidth, font, color);
                charPositions.add(charPosition);
            }
        }
    }
}