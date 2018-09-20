package ru.icc.td.tabbypdf2.debug;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

import ru.icc.td.tabbypdf2.model.*;

import java.awt.geom.Line2D;
import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

public final class DebuggingDrawer {
    private static final String SUFFIX_SEPARATOR = "_";

    private Document document;
    private Path debugDirectoryPath;

    public DebuggingDrawer() {
    }

    public void drawTo(Document document, Path debugDirectoryPath) throws IOException {
        this.document = document;
        this.debugDirectoryPath = debugDirectoryPath;

        File file = document.getSourceFile();

        PDDocument pdDocument = loadPDDocument(file);
        PDFContentDrawer contentDrawer = new PDFContentDrawer(pdDocument);

        drawDocument(document, contentDrawer);

        File outputFile = createOutputFile(null, null);
        pdDocument.save(outputFile);
        pdDocument.close();
    }

    private PDDocument loadPDDocument(File file) {
        PDDocument result = null;

        try {
            result = PDDocument.load(file);

            if (result.isEncrypted())
                result.setAllSecurityToBeRemoved(true);

            return result;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    private File createOutputFile(String subDirName, String fileNameSuffix) {
        // Make the specified output directory
        Path outputDirectoryPath = debugDirectoryPath;

        if (null != subDirName)
            outputDirectoryPath = debugDirectoryPath.resolve(subDirName);

        outputDirectoryPath.toFile().mkdirs();

        // Build the output file path
        String suffix = null == fileNameSuffix ? "" : SUFFIX_SEPARATOR.concat(fileNameSuffix);

        File file = document.getSourceFile();
        String fileName = FilenameUtils.removeExtension(file.getName());

        return new File(String.format("%s/%s%s.pdf", outputDirectoryPath, fileName, suffix));
    }

    private void drawDocument(Document document, PDFContentDrawer contentDrawer) throws IOException {
        for (Page page : document.getPages())
            drawPage(page, contentDrawer);
    }

    private void drawPage(Page page, PDFContentDrawer contentDrawer) throws IOException {
        int pageIndex = page.getIndex();
        contentDrawer.startPage(pageIndex);

        drawCursorTraces(page, contentDrawer);
        drawImageBounds(page, contentDrawer);

        drawBlocks(page, contentDrawer);
        drawWords(page, contentDrawer);
        //drawCharPositions(page, contentDrawer);
        //drawGaps(page, contentDrawer);

        contentDrawer.endPage();
    }

    private void drawCharPositions(Page page, PDFContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.BLACK, null, 0.25f);

        for (CharPosition charPosition : page.getCharPositions())
            contentDrawer.strokeRectangle(charPosition);
    }

    private void drawWords(Page page, PDFContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.BLUE, Color.RED, 0.25f);

        for (Word word : page.getWords()) {
            contentDrawer.strokeRectangle(word);
            String text = String.valueOf(word.getStartChunkID());
            contentDrawer.showText(text, word.x, (float) word.getMaxY());
        }
    }

    private void drawBlocks(Page page, PDFContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.BLUE, null, 1.0f);

        for (Block block : page.getBlocks())
            contentDrawer.strokeRectangle(block);
    }

    private void drawCursorTraces(Page page, PDFContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.GREEN, null, 0.5f);

        for (CursorTrace cursorTrace : page.getCursorTraces())
            contentDrawer.strokeLine(cursorTrace);
    }

    private void drawImageBounds(Page page, PDFContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.PINK, null, 2.0f);

        for (Rectangle2D imageBBox : page.getImageBounds())
            contentDrawer.strokeRectangle(imageBBox);
    }

    private void drawGaps(Page page, PDFContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.GRAY, Color.GRAY, 2.0f);

        List<CursorTrace> leftCursorTraces = page.getGap().getLeft();
        for (CursorTrace cursorTrace : leftCursorTraces) {
            contentDrawer.strokeLine(new Line2D.Double(cursorTrace.x1, cursorTrace.y1, cursorTrace.x2, cursorTrace.y2));
        }

        contentDrawer.setStyle(Color.RED, null, 2.0f);

        List<CursorTrace> rightCursorTrace = page.getGap().getRight();
        for (CursorTrace cursorTrace : rightCursorTrace) {
            contentDrawer.strokeLine(new Line2D.Double(cursorTrace.x1, cursorTrace.y1, cursorTrace.x2, cursorTrace.y2));
        }
    }

}
