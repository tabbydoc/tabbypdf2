package ru.icc.td.tabbypdf2.debug;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;

import ru.icc.td.tabbypdf2.model.*;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

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
        ContentDrawer contentDrawer = new ContentDrawer(pdDocument);

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

    private void drawDocument(Document document, ContentDrawer contentDrawer) throws IOException {
        for (Page page : document.getPages())
            drawPage(page, contentDrawer);
    }

    private void drawPage(Page page, ContentDrawer contentDrawer) throws IOException {
        int pageIndex = page.getIndex();
        contentDrawer.startPage(pageIndex);

        //drawCharPositions(page, contentDrawer);
        drawWords(page, contentDrawer);
        drawBlocks(page, contentDrawer);
        drawRulings(page, contentDrawer);

        contentDrawer.endPage();
    }

    private void drawCharPositions(Page page, ContentDrawer contentDrawer) throws IOException {
        for (CharPosition charPosition : page.getCharPositions())
            contentDrawer.strokeRectangle(charPosition);
    }

    private void drawWords(Page page, ContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.BLUE, Color.RED, 0.25f);

        for (Word word : page.getWords()) {
            contentDrawer.strokeRectangle(word);
            String text = String.valueOf(word.getStartChunkID());
            contentDrawer.showText(text, word.x, (float) word.getMaxY());
        }
    }

    private void drawBlocks(Page page, ContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.BLACK, Color.RED, 0.5f);

        for (Block block : page.getBlocks())
            contentDrawer.strokeRectangle(block);
    }

    private void drawRulings(Page page, ContentDrawer contentDrawer) throws IOException {
        contentDrawer.setStyle(Color.GREEN, Color.GREEN, 0.5f);

        for (Ruling ruling : page.getRulings())
            contentDrawer.strokeLine(ruling);
    }


}