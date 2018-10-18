package ru.icc.td.tabbypdf2.read;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class DocumentLoader {

    private PDFTextExtractor PDFTextExtractor;
    private PDFGraphicsExtractor PDFGraphicsExtractor;
    private PDFRulingExtractor PDFRulingExtractor;

    public Document load(Path path) throws IllegalArgumentException, IOException {
        if (null == path) {
            throw new IllegalArgumentException("The path to a PDF document cannot be null");
        }
        else {
            File file = path.toFile();
            if (null == file)
                return null;

            if (file.exists() && file.canRead()) {
                PDDocument pdDocument = PDDocument.load(file);

                PDFTextExtractor = new PDFTextExtractor(pdDocument);
                PDFGraphicsExtractor = new PDFGraphicsExtractor(pdDocument);
                PDFRulingExtractor = new PDFRulingExtractor(pdDocument);

                Document document = createDocument(file, pdDocument);
                pdDocument.close();
                return document;
            }
            else
                return null;
        }
    }

    private Document createDocument(File file, PDDocument pdDocument) throws IllegalArgumentException, IOException {
        Document result = new Document(file, pdDocument);
        createPages(result);
        return result;
    }

    private void createPages(Document document) throws IOException {
        PDDocument pdDocument = document.getPDDocument();
        int size = pdDocument.getNumberOfPages();
        for (int i = 0; i < size; i++) {
            Page page = createPage(document, i);
            document.addPage(page);
        }
    }

    private Page createPage(Document document, int pageIndex) throws IOException {
        PDDocument pdDocument = document.getPDDocument();
        PDPage pdPage = pdDocument.getPage(pageIndex);

        if (null != pdPage) {

            PDRectangle rect = pdPage.getBBox();

            float lt = rect.getLowerLeftX();
            //float tp = rect.getUpperRightY();
            float tp = rect.getLowerLeftY();
            float rt = rect.getUpperRightX();
            //float bm = rect.getLowerLeftY();
            float bm = rect.getUpperRightY();

            Rectangle2D.Float bbox = new Rectangle2D.Float(lt, tp, rt - lt, bm - tp);
            Page page = new Page(document, pageIndex, bbox);

            if (pdPage.getRotation() == 90) {
                page.setOrientation(Page.Orientation.LANDSCAPE);
            }

            PDFTextExtractor.readTo(pageIndex, page);
            PDFGraphicsExtractor.readTo(pageIndex, page);
            PDFRulingExtractor.readTo(pageIndex, page);

            return page;
        }
        return null;
    }
}
