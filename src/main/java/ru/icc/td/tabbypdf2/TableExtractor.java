package ru.icc.td.tabbypdf2;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import ru.icc.td.tabbypdf2.config.AppConfig;
import ru.icc.td.tabbypdf2.debug.DebuggingDrawer;
import ru.icc.td.tabbypdf2.comp.DocumentComposer;
import ru.icc.td.tabbypdf2.detect.PdfToImage;
import ru.icc.td.tabbypdf2.detect.RcnnTableDetector;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Table;
import ru.icc.td.tabbypdf2.read.DocumentLoader;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public final class TableExtractor {

    RcnnTableDetector tableDetector;

    // CLI params

    @Option(name = "-i",
            aliases = {"--input"},
            required = true,
            metaVar = "PATH",
            usage = "specify the path to a source file or directory of files (*.pdf)")
    private String inputArg;
    private File inputFile;
    private Path inputPath;

    @Option(name = "-o",
            aliases = {"--output"},
            metaVar = "PATH",
            usage = "specify the path to a directory for extracted data")
    private String outputArg;
    private File outputFile;
    private Path outputPath;

    @Option(name = "-d",
            aliases = {"--debug"},
            usage = "enable debug")
    private boolean useDebug = true;
    private Path debugPath;

    @Option(name = "-?",
            aliases = {"--help"},
            usage = "show this message")

    private boolean help = false;

    public static void main(String[] args) throws Exception {
        new TableExtractor().run(args);
    }

    private void throwIfEmpty(String arg) {
        if (isEmptyArg(arg))
            throw new IllegalArgumentException("A required option was not specified");
    }

    private boolean isEmptyArg(String arg) {
        return arg == null || arg.isEmpty();
    }

    public void run(String[] args) throws Exception {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);

            if (help) {
                parser.printUsage(System.err);
                System.exit(0);
            }

            throwIfEmpty(inputArg);

            inputFile = new File(inputArg);

            if (!inputFile.exists()) {
                System.err.println("The specified path to the source (*.pdf) files does not exist");
                System.exit(0);
            } else {
                if (!inputFile.canRead()) {
                    System.err.println("The specified path to the source (*.pdf) files can not be read");
                    System.exit(0);
                }
            }

            inputPath = inputFile.isFile() ? inputFile.getParentFile().toPath() : inputFile.toPath();

            if (isEmptyArg(outputArg))
                outputFile = inputPath.resolve("extracted").toFile();
            else
                outputFile = new File(outputArg);

            outputFile.mkdirs();
            outputPath = outputFile.toPath();

            if (useDebug)
                debugPath = outputPath.resolve("debug");

            boolean useAnnModel = AppConfig.isUseANNModel();
            if (useAnnModel) {
                Path pathToModel = Paths.get(AppConfig.getPathToANNModel());
                Path pathToLabelMap = Paths.get(AppConfig.getPathToLabelMap());
                tableDetector = new RcnnTableDetector(pathToModel, pathToLabelMap);
            } else {
                tableDetector = null;
            }

            final String[] extensions = {"pdf", "PDF"};

            if (inputFile.isFile()) {
                if (FilenameUtils.isExtension(inputFile.getName(), extensions)) {
                    System.out.println(inputFile.getCanonicalPath());
                    processDocument(inputFile);
                } else {
                    System.err.println("The extension of the specified file is not PDF");
                    System.exit(0);
                }
            } else if (inputFile.isDirectory()) {
                Collection<File> files = FileUtils.listFiles(inputFile, extensions, true);
                if (files.isEmpty()) {
                    System.err.println("There are no (*.pdf) files in the specified directory");
                } else {
                    for (File file : files) {
                        System.out.println(file.getCanonicalPath());
                        processDocument(file);
                    }
                }
            } else {
                System.err.println("The specified path to the source (*.pdf) files is not valid");
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            parser.printUsage(System.err);
            System.exit(1);
        }
    }

    private Document loadDocument(File file) throws IOException {
        return new DocumentLoader().load(file.toPath());
    }

    private Document recomposeDocument(Document originDocument) {
        return null;
    }

    private List<Table> extractTables(Document recomposedDocument) throws IOException {
        //Extract all tables from the document
        if (recomposedDocument == null)
            return null;

        ArrayList<Page> pages = (ArrayList<Page>) recomposedDocument.getPages();
        if (pages.isEmpty())
            return null;

        PdfToImage pdfToImage = new PdfToImage(recomposedDocument.getSourceFile());

        List<Rectangle2D> tables;
        for (Page page: pages) {
            BufferedImage img = pdfToImage.getImageForPage(page.getIndex());
            tables = tableDetector.detectTables(img);
        }

        return null;
    }

    private void writeTables(List<Table> tables) {
    }

    private void processDocument(File file) {
        Document originDocument = null;
        Document recomposedDocument = null;

        try {
            originDocument = loadDocument(file);

            DocumentComposer documentComposer = new DocumentComposer();
            documentComposer.compose(originDocument);

            recomposedDocument = recomposeDocument(originDocument);
            List<Table> tables = extractTables(originDocument);
            writeTables(tables);

            if (useDebug) {
                DebuggingDrawer debuggingDrawer = new DebuggingDrawer();
                debuggingDrawer.drawTo(originDocument, debugPath);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}



