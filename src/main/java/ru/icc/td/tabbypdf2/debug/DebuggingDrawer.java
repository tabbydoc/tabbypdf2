package ru.icc.td.tabbypdf2.debug;

import org.apache.commons.io.FilenameUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import ru.icc.td.tabbypdf2.detect.processing.recognition.Projection;
import ru.icc.td.tabbypdf2.extract.Segment;
import ru.icc.td.tabbypdf2.extract.TableProjection;
import ru.icc.td.tabbypdf2.model.*;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Path;
import java.util.Hashtable;
import java.util.List;
import java.util.Properties;
import java.util.Set;

public final class DebuggingDrawer {
    private static final String SUFFIX_SEPARATOR = "_";

    private final Properties properties = new Properties();
    private final Hashtable<String, Boolean> elements = new Hashtable<>();
    private final Class obj = this.getClass();
    private String[] order;
    private Method[] methods;

    private Document document;
    private Path debugDirectoryPath;

    public DebuggingDrawer() {
        loadProperties();
        this.methods = obj.getDeclaredMethods();
    }

    private void loadProperties() {
        ClassLoader classLoader = getClass().getClassLoader();

        try (InputStream inputStream = classLoader.getResourceAsStream("dd.properties")) {
            properties.load(inputStream);

            for(String key : properties.stringPropertyNames()){
                boolean value = properties.getProperty(key).toLowerCase().equals("true");
                elements.put(key, value);
            }

            order = properties.getProperty("order").split(",\\s");

        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
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
        for (Page page : document.getPages()) {
            drawPage(page, contentDrawer);
        }
    }

    private void drawPage(Page page, PDFContentDrawer contentDrawer) throws IOException {
        int pageIndex = page.getIndex();
        contentDrawer.startPage(pageIndex);

        Color strokeColor, fillColor;
        Field f;
        float lineWidth;

        for(String o : order){

            for(Method m : methods){

                if(m.getName().toLowerCase().contains(o.toLowerCase()) && elements.get(o)){
                    m.setAccessible(true);
                    String strokeC = properties.getProperty(o + " StrokeColor");
                    String fillC = properties.getProperty(o + " FillColor");
                    String lineW = properties.getProperty(o + " LineWidth");

                    try {
                        if(!strokeC.contains("null")) {
                            f = Color.class.getField(strokeC);
                            strokeColor = (Color) f.get(null);
                        } else
                            strokeColor = null;

                        if(!fillC.contains("null")) {
                            f = Color.class.getField(fillC);
                            fillColor = (Color) f.get(null);
                        } else
                            fillColor = null;

                        lineWidth = Float.parseFloat(lineW);
                        contentDrawer.setStyle(strokeColor, fillColor, lineWidth);

                        m.invoke(obj.getDeclaredConstructor().newInstance(), page, contentDrawer);
                    } catch (InvocationTargetException e) {
                        Throwable exception = e.getTargetException();
                        exception.printStackTrace();
                    } catch (Exception e){
                        e.printStackTrace();
                        System.exit(1);
                    }

                }
            }
        }

        contentDrawer.endPage();
    }

    private void drawCharPositions(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (CharPosition charPosition : page.getCharPositions())
            contentDrawer.strokeRectangle(charPosition);
    }

    private void drawWords(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Word word : page.getWords()) {
            contentDrawer.strokeRectangle(word);
            String text = String.valueOf(word.getStartChunkID());
            contentDrawer.showText(text, (float) word.getX(), (float) word.getMaxY());
        }
    }

    private void drawBlocks(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Block block : page.getBlocks()) {
            contentDrawer.strokeRectangle(block);
        }
    }

    private void drawLines(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Line line : page.getLines())
            contentDrawer.strokeRectangle(line);
    }

    private void drawCursorTraces(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (CursorTrace cursorTrace : page.getCursorTraces()) {
            if (cursorTrace.isVertical())
                contentDrawer.strokeLine(cursorTrace);
        }
    }

    private void drawImageBounds(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Rectangle2D imageBBox : page.getImageBounds())
            contentDrawer.strokeRectangle(imageBBox);
    }

    private void drawGaps(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Rectangle2D rect : page.getGaps()) {
            //contentDrawer.fillRectangle(rect);
            contentDrawer.strokeRectangle(rect);
        }
    }

    private void drawRulings(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Ruling ruling : page.getRulings())
            contentDrawer.strokeLine(ruling);
    }

    private void drawTables(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Table table : page.getTables()) {
            contentDrawer.strokeRectangle(table);
            for(Segment projection: table.getRows()) {
                contentDrawer.strokeLine(new Line2D.Double(table.getMinX() + 5, projection.getStart(), table.getMaxX() - 5, projection.getStart()));
                contentDrawer.strokeLine(new Line2D.Double(table.getMinX() + 5, projection.getEnd(), table.getMaxX() - 5, projection.getEnd()));
            }

            for(Segment projection: table.getColumns()) {
                contentDrawer.strokeLine(new Line2D.Double(projection.getStart(), table.getMinY() + 5, projection.getStart(), table.getMaxY() - 5));
                contentDrawer.strokeLine(new Line2D.Double(projection.getEnd(), table.getMinY() + 5, projection.getEnd(), table.getMaxY() - 5));
            }

            /*if (table.getMap() == null) {
                return;
            }

            Set<Projection.Horizontal> horizontals = table.getMap().keySet();

            for (Projection.Horizontal horizontal : horizontals) {
                contentDrawer.strokeLine(horizontal);

                for (Projection.Vertical vertical : table.getMap().get(horizontal)) {
                    contentDrawer.strokeLine(vertical);
                }
            }*/


            List<Projection.Vertical> verticals = table.getVerticals();

            if (verticals == null) {
                return;
            }

            for (Projection.Vertical vertical : verticals) {
                contentDrawer.strokeLine(vertical);
            }
        }
    }

    private void drawGraphs(Page page, PDFContentDrawer contentDrawer) throws IOException {
        for (Table table : page.getTables()){
            Graph<Block, DefaultWeightedEdge> graph = table.getStructure();

            if (graph == null) {
                return;
            }

            Set<Block> blocks = graph.vertexSet();

            if (blocks == null) {
                return;
            }

            for (Block blockI : blocks) {

                for (Block blockJ : blocks) {

                    if(blockI.equals(blockJ)) {
                        continue;
                    }

                    DefaultWeightedEdge edge = graph.getEdge(blockI, blockJ);

                    if (edge != null) {
                        Point2D pointI = new Point2D.Double(blockI.getCenterX(), blockI.getCenterY());
                        Point2D pointJ = new Point2D.Double(blockJ.getCenterX(), blockJ.getCenterY());
                        Line2D line = new Line2D.Double();
                        line.setLine(pointI, pointJ);

                        contentDrawer.strokeLine(line);
                        float x = (float) Math.abs(line.getX1() + line.getX2())/2;
                        float y = (float) Math.abs(line.getY1() + line.getY2())/2;
                        contentDrawer.showText(String.valueOf(graph.getEdgeWeight(edge)), x, y);
                    }
                }
            }
        }
    }
}
