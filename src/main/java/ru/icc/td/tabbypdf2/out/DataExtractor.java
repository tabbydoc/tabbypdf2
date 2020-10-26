package ru.icc.td.tabbypdf2.out;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import ru.icc.td.tabbypdf2.config.AppConfig;
import ru.icc.td.tabbypdf2.detect.processing.PredictionProcessing;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Prediction;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.*;

public class DataExtractor {
    private Document document;
    private final String mode;

    private List<Chunk> chunks;
    private List<String> cells;

    public DataExtractor(String mode) {
        this.mode = mode;
    }

    public void start(Document document) {
        if (document == null)
            return;

        this.document = document;

        ArrayList<Page> pages = (ArrayList<Page>) document.getPages();
        if (pages.isEmpty())
            return;
        chunks = new ArrayList<>();
        cells = new ArrayList<>();
        PredictionProcessing processing;

        switch (mode) {
            case "SciTSR":
                processing = new PredictionProcessing(false, false);
                break;
            case "NEGATIVE":
            case "ICDAR":
                processing = new PredictionProcessing(AppConfig.isUseVerification(), AppConfig.isUseRefinement());
                try {
                    extractChunks();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + mode);
        }

        for (Page page : pages) {
            switch (mode) {
                case "SciTSR":
                    for (Rectangle2D.Double rect : getTables()) {
                        Prediction prediction = new Prediction(rect, page);
                        processing.process(prediction);
                        page.addTable(processing.getTable());
                    }
                    break;
                case "NEGATIVE":
                    for (Prediction prediction : getTables(page)) {
                        processing.process(prediction);
                        page.addTable(processing.getTable());
                    }
                    break;
                case "ICDAR":
                    for (Chunk chunk : chunks) {
                        if (chunk.pageIndex == page.getIndex()) {
                            Rectangle2D.Double rect = new Rectangle2D.Double();
                            rect.setRect(chunk.x1, chunk.y1, chunk.x2 - chunk.x1, chunk.y2 - chunk.y1);
                            Prediction prediction = new Prediction(rect, page);
                            processing.process(prediction);

                            page.addTable(processing.getTable());
                        }
                    }
                    break;
            }
        }

        chunks.clear();
        cells.clear();
    }

    private List<Prediction> getTables(Page page) {
        List<Prediction> predictions = new ArrayList<>();
        double h = page.getHeight();
        double w = page.getWidth();
        double alpha = h / 5;
        double beta = h;
        double gamma = w / 5;
        double delta = w;

        double a = 0, b = 0, x = 0, y = 0;

        double S = h * w * 0.05;
        Random random = new Random();
        int counter = 0;
        int NEB = 0;

        while (counter < 1) {
            while (a * b <= S) {
                a = random.nextDouble() * (beta - alpha) + alpha;
                b = random.nextDouble() * (delta - gamma) + gamma;
            }

            x = random.nextDouble() * w;
            y = random.nextDouble() * h;

            Rectangle2D.Double rect = new Rectangle2D.Double(x, y, b, a);
            if (isTable(rect, page)) {
                Prediction prediction = new Prediction(rect, page);

                if (prediction.width * prediction.height < S && NEB < 10) {
                    NEB++;
                    continue;
                }

                if (prediction.getBlocks().size() >= 1 && !prediction.getBlocks().isEmpty()) {
                    predictions.add(prediction);
                    counter++;
                    NEB = 0;
                }
            }
        }

        return predictions;
    }

    private void extractChunks() throws
            IOException, SAXException, ParserConfigurationException {
        String parentPath = FilenameUtils.getFullPath(document.getSourceFile().toString());
        String name = FilenameUtils.getBaseName(document.getFileName());
        File file = new File(String.format("%s/%s-reg-output.xml", parentPath, name));

        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            org.w3c.dom.Document doc = dBuilder.parse(file);

            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("table");

            for (int i = 0; i < nList.getLength(); i++) {

                Node nNode = nList.item(i);
                String text = nNode.getAttributes().getNamedItem("id").toString();

                if (nNode.getNodeType() == Node.ELEMENT_NODE) {

                    Element element = (Element) nNode;

                    NodeList regions = element.getElementsByTagName("region");

                    for (int j = 0; j < regions.getLength(); j++) {
                        Element jNode = (Element) regions.item(j);
                        NamedNodeMap map = jNode.getElementsByTagName("bounding-box").item(0).getAttributes();

                        double x1 = Double.parseDouble(map.getNamedItem("x1").getNodeValue());
                        double y1 = Double.parseDouble(map.getNamedItem("y1").getNodeValue());
                        double x2 = Double.parseDouble(map.getNamedItem("x2").getNodeValue());
                        double y2 = Double.parseDouble(map.getNamedItem("y2").getNodeValue());
                        Chunk chunk = new Chunk(x1, x2, y1, y2, text);
                        chunk.pageIndex = Integer.parseInt(jNode.getAttribute("page")) - 1;
                        chunks.add(chunk);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isTable(Rectangle2D.Double rectangle, Page page) {
        for (Chunk chunk : chunks) {
            if (chunk.pageIndex == page.getIndex()) {
                Rectangle2D.Double chunkR = new Rectangle2D.Double(chunk.x1, chunk.y1,
                        Math.abs(chunk.x2 - chunk.x1), Math.abs(chunk.y1 - chunk.y2));

                Rectangle2D.Double intersection = new Rectangle2D.Double();

                Rectangle2D.Double.intersect(rectangle, chunkR, intersection);

                double s1 = rectangle.width * rectangle.height;
                double s2 = intersection.width * intersection.height;

                if (s2 / s1 >= 0.04) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<Rectangle2D.Double> getTables() {
        List<Rectangle2D.Double> tables = new ArrayList<>();

        try {
            setByKey("chunks");
            setByKey("cells");
            check();

            if (!chunks.isEmpty()) {
                double x1 = Collections.min(chunks, Comparator.comparing(Chunk::getX1)).x1;
                double y1 = Collections.min(chunks, Comparator.comparing(Chunk::getY1)).y1;
                double x2 = Collections.max(chunks, Comparator.comparing(Chunk::getX2)).x2;
                double y2 = Collections.max(chunks, Comparator.comparing(Chunk::getY2)).y2;

                tables.add(new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1));
            } else {
                System.err.printf("Chunks is empty in the document %s\n", FilenameUtils.getName(document.getFileName()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tables;
    }

    private void setByKey(String key) throws IOException {
        boolean isChunks = key.equals("chunks");
        File file;
        String parentPath = FilenameUtils.getFullPath(document.getSourceFile().getParent());
        String name = FilenameUtils.getBaseName(document.getFileName());

        if (isChunks) {
            file = new File(String.format("%s/%s/%s.%s", parentPath, "chunk", name, "chunk"));
        } else {
            file = new File(String.format("%s/%s/%s.%s", parentPath, "structure", name, "json"));
        }

        String fileString = FileUtils.readFileToString(file, "utf-8");
        JSONObject jsonObject = new JSONObject(fileString);
        JSONArray jsonArray = jsonObject.getJSONArray(key);

        if (isChunks) {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                JSONArray pos = obj.getJSONArray("pos");

                chunks.add(new Chunk(pos.getDouble(0), pos.getDouble(1),
                        pos.getDouble(2), pos.getDouble(3), getWords(obj.getString("text"))));
            }
        } else {
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                StringBuilder stringBuilder = new StringBuilder();
                JSONArray strings = obj.getJSONArray("content");

                for (int j = 0; j < strings.length(); j++) {
                    stringBuilder.append(strings.getString(j)).append(" ");
                }

                String string = getWords(stringBuilder.toString());
                cells.add(string);
            }
        }
    }

    private void check() {
        for (int i = 0; i < chunks.size(); i++) {
            Chunk chunk = chunks.get(i);
            String chunkText = chunk.text;

            boolean contains = false;

            for (String cell : cells) {

                if (chunkText.equals(cell)) {
                    contains = true;
                    break;
                }
            }

            if (!contains) {
                chunks.remove(i);
                i--;
            }
        }
    }

    public String getWords(String text) {
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
        String word = "";
        while (BreakIterator.DONE != lastIndex) {
            int firstIndex = lastIndex;
            lastIndex = breakIterator.next();
            if (lastIndex != BreakIterator.DONE && Character.isLetterOrDigit(text.charAt(firstIndex))) {
                word = word.concat(text.substring(firstIndex, lastIndex));
            }
        }

        return word;
    }

    private static class Chunk {
        double x1;
        double x2;
        double y1;
        double y2;
        String text;
        int pageIndex;

        public Chunk(double x1, double x2, double y1, double y2, String text) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.text = text;
        }

        public double getX1() {
            return x1;
        }

        public double getX2() {
            return x2;
        }

        public double getY1() {
            return y1;
        }

        public double getY2() {
            return y2;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x1, x2, y1, y2, text);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Chunk)) return false;
            Chunk chunk = (Chunk) o;
            return Double.compare(chunk.x1, x1) == 0 &&
                    Double.compare(chunk.x2, x2) == 0 &&
                    Double.compare(chunk.y1, y1) == 0 &&
                    Double.compare(chunk.y2, y2) == 0 &&
                    text.equals(chunk.text);
        }
    }
}
