package ru.icc.td.tabbypdf2.detect;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class PdfToImage {

    private PDFRenderer renderer;

    public PdfToImage(File pdf) throws IOException {
        PDDocument document;
        document = PDDocument.load(pdf);
        renderer = new PDFRenderer(document);
    }

    public BufferedImage getImageForPage(int pageNumber) throws IOException {
        BufferedImage img = renderer.renderImageWithDPI(pageNumber, 72, ImageType.RGB);
        BufferedImage convertedImg = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_3BYTE_BGR);
        convertedImg.getGraphics().drawImage(img, 0, 0, null);
        return convertedImg;
    }

}
