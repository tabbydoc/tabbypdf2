package ru.icc.td.tabbypdf2;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import ru.icc.td.tabbypdf2.comp.DocumentComposer;
import ru.icc.td.tabbypdf2.config.AppConfig;
import ru.icc.td.tabbypdf2.debug.DebuggingDrawer;
import ru.icc.td.tabbypdf2.detect.PdfToImage;
import ru.icc.td.tabbypdf2.detect.RcnnTableDetector;
import ru.icc.td.tabbypdf2.detect.processing.PredictionProcessing;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Prediction;
import ru.icc.td.tabbypdf2.model.Table;
import ru.icc.td.tabbypdf2.out.DataExtractor;
import ru.icc.td.tabbypdf2.out.ExcelWriter;
import ru.icc.td.tabbypdf2.out.XmlWriter;
import ru.icc.td.tabbypdf2.read.DocumentLoader;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ru.icc.td.tabbypdf2.util.Utils.*;

public final class TableExtractor {

    static {
        nu.pattern.OpenCV.loadLocally();
    }

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
    private final boolean useDebug = true;
    private Path debugPath;

    @Option(name = "-?",
            aliases = {"--help"},
            usage = "show this message")

    private final boolean help = false;

    private ExcelWriter excelWriter;
    private DataExtractor dataExtractor;

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

    private boolean useAnnModel;
    private boolean saveToExcel;
    private boolean useExtractor;

    private Document loadDocument(File file) throws IOException {
        return new DocumentLoader().load(file.toPath());
    }

    private Document recomposeDocument(Document originDocument) {
        return null;
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

            useAnnModel = AppConfig.isUseANNModel();
            if (useAnnModel) {
                Path pathToModel = Paths.get(AppConfig.getPathToANNModel());
                Path pathToLabelMap = Paths.get(AppConfig.getPathToLabelMap());
                tableDetector = new RcnnTableDetector(pathToModel, pathToLabelMap);
            } else {
                tableDetector = null;
            }

            // Excel
            saveToExcel = AppConfig.isSaveToExcel();
            if (saveToExcel) {
                excelWriter = new ExcelWriter(debugPath);
            }

            // Extractor
            useExtractor = AppConfig.isUseExtractor();
            if (useExtractor) {
                dataExtractor = new DataExtractor(AppConfig.getExtractorMode());
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
                    int i = 1;
                    int size = files.size();

                    for (File file : files) {
                        int percent = Math.round((i * 100) / size);
                        System.out.printf("%d / %d %d%% %s\n",
                                i, size, percent, file.getCanonicalPath());
                        i++;

                        processDocument(file);
                    }
                }
            } else {
                System.err.println("The specified path to the source (*.pdf) files is not valid");
            }

            if (saveToExcel) {
                excelWriter.save();
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

    private boolean extractTables(Document recomposedDocument) throws IOException {
        //Extract all tables from the document
        if (recomposedDocument == null)
            return false;

        ArrayList<Page> pages = (ArrayList<Page>) recomposedDocument.getPages();
        if (pages.isEmpty())
            return false;

        PdfToImage pdfToImage = new PdfToImage(recomposedDocument.getSourceFile());

        PredictionProcessing processing = new PredictionProcessing(AppConfig.isUseVerification(),
                AppConfig.isUseRefinement());

        List<Rectangle2D> tables = null;
        for (Page page : pages) {
            BufferedImage img = pdfToImage.getImageForPage(page.getIndex());
            Mat imgResult = matify(img);
            Mat bw = new Mat();
            Imgproc.cvtColor(imgResult, bw, Imgproc.COLOR_BGR2GRAY);
            Imgproc.threshold(bw, bw, 40, 255, Imgproc.THRESH_BINARY | Imgproc.THRESH_OTSU);
            Mat r = new Mat();
            Mat g = new Mat();
            Mat b = new Mat();
            List<Mat> results = new ArrayList<>();
            Imgproc.distanceTransform(bw, r, Imgproc.DIST_L2, 5);
            Imgproc.distanceTransform(bw, g, Imgproc.DIST_L1, 5);
            Imgproc.distanceTransform(bw, b, Imgproc.DIST_C, 5);
            results.add(r);
            results.add(g);
            results.add(b);
            Core.merge(results, imgResult);
            img = mat2BufferedImage(imgResult);
            tables = tableDetector.detectTables(img);
            if (tables == null)
                continue;
            if (tables.isEmpty())
                continue;

            for (Rectangle2D rect : tables) {
                Prediction prediction = new Prediction(rect, page);
                processing.process(prediction);
                page.addTable(processing.getTable());
            }
        }

        recomposedDocument.close();

        return true;
    }

    private void writeTables(List<Table> tables, FileWriter file, String fileName) throws IOException {
        XmlWriter xmlWriter = new XmlWriter(tables, fileName);
        List<String> icdarTables = xmlWriter.write();
        if (icdarTables != null) {
            String iTables = String.join(" ", icdarTables);
            file.write(iTables);
            file.flush();
        }
    }

    private void processDocument(File file) {
        Document originDocument = null;
        Document recomposedDocument = null;

        try {
            originDocument = loadDocument(file);

            DocumentComposer documentComposer = new DocumentComposer();
            documentComposer.compose(originDocument);
            recomposedDocument = recomposeDocument(originDocument);

            if (useExtractor) {
                dataExtractor.start(originDocument);
                excelWriter.writeExcel(originDocument);
            } else if (extractTables(originDocument)) {
                if (saveToExcel) {
                    excelWriter.writeExcel(originDocument);
                }
            }

            if (AppConfig.isSaveToIcdar()) {
                File out = createOutputFile(file, "xml", debugPath, "-reg-output", "xml");
                FileWriter fileWriter = new FileWriter(out);
                List<Table> tables = new ArrayList<>();
                for (Page page : originDocument.getPages()) {
                    tables.addAll(page.getTables());
                }
                writeTables(tables, fileWriter, originDocument.getFileName());
                fileWriter.close();
            }

            if (useDebug) {
                DebuggingDrawer debuggingDrawer = new DebuggingDrawer();
                debuggingDrawer.drawTo(originDocument, debugPath);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (originDocument != null) {
                    originDocument.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}