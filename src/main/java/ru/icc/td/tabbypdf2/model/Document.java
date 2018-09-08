package ru.icc.td.tabbypdf2.model;

import org.apache.pdfbox.pdmodel.PDDocument;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Document implements Closeable {
    private final File sourceFile;
    private final PDDocument pdDocument;
    private final List<Page> pages = new ArrayList();

    public Document(File sourceFile, PDDocument pdDocument) throws IllegalArgumentException, IOException {
        if (null == pdDocument) {
            throw new IllegalArgumentException("The original PDDocument cannot be null");
        }
        else {
            this.sourceFile = sourceFile;
            this.pdDocument = pdDocument;
        }
    }

    public File getSourceFile() {
        return sourceFile;
    }

    public PDDocument getPDDocument() {
        return pdDocument;
    }

    public void addPage(Page page) {
        pages.add(page);
    }

    public List<Page> getPages() {
        return pages;
    }

    @Override
    public void close() throws IOException {
        pdDocument.close();
    }
}
