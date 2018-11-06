package ru.icc.td.tabbypdf2.util;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.pdfbox.tools.imageio.ImageIOUtil;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;

public final class PDF2ImageConverter {

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
            usage = "specify the path to a directory for generated page images")
    private String outputArg;
    private File outputFile;
    private Path outputPath;

    @Option(name = "-f",
            aliases = {"--format"},
            metaVar = "FORMAT",
            usage = "specify the file format for generated page images (e.g. png, jpg, gif)")
    private String imageFileFormatArg;
    private String imageFileFormat;

    public static void main(String[] args) {
        new PDF2ImageConverter().run(args);
    }

    private void throwIfEmpty(String arg) {
        if (isEmptyArg(arg))
            throw new IllegalArgumentException("A required option was not specified");
    }

    private boolean isEmptyArg(String arg) {
        return arg == null || arg.isEmpty();
    }

    public void run(String[] args) {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);

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
                outputFile = inputPath.resolve("images").toFile();
            else
                outputFile = new File(outputArg);

            outputFile.mkdirs();
            outputPath = outputFile.toPath();

            if (isEmptyArg(imageFileFormatArg))
                imageFileFormat = "png";
            else
                imageFileFormat = imageFileFormatArg;

            final String[] extensions = {"pdf", "PDF"};

            if (inputFile.isFile()) {
                if (FilenameUtils.isExtension(inputFile.getName(), extensions)) {
                    System.out.println(inputFile.getCanonicalPath());
                    convertDocument(inputFile);
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
                        convertDocument(file);
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

    private void convertDocument(File pdfFile) {
        PDDocument document;
        try {
            document = PDDocument.load(pdfFile);
            PDFRenderer pdfRenderer = new PDFRenderer(document);

            for (int i = 0; i < document.getNumberOfPages(); i ++) {
                BufferedImage image = pdfRenderer.renderImageWithDPI(i, 150, ImageType.RGB);

                String pdfFileBaseName = FilenameUtils.getBaseName(pdfFile.getName());
                String imageFileName = String.format("%s_%03d.%s", pdfFileBaseName, i, imageFileFormat);
                Path outputImagePath = Paths.get(outputFile.getCanonicalPath(), imageFileName);

                ImageIOUtil.writeImage(image, outputImagePath.toString(), 150);
            }

            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}


