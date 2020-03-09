package ru.icc.td.tabbypdf2;

import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import ru.icc.td.tabbypdf2.detect.processing.PredictionProcessing;
import ru.icc.td.tabbypdf2.model.Document;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.model.Prediction;

import java.awt.geom.Rectangle2D;
import java.io.File;
import java.io.IOException;
import java.text.BreakIterator;
import java.util.*;

public class DataExtractor {
    private Document document;

    private List<Chunk> chunks = new ArrayList<>();
    private List<String> cells = new ArrayList<>();

    public void start(Document document) {
        if (document == null)
            return;

        this.document = document;

        ArrayList<Page> pages = (ArrayList<Page>) document.getPages();
        if (pages.isEmpty())
            return;

        PredictionProcessing processing = new PredictionProcessing(false);

        for (Page page : pages) {
            List<Rectangle2D.Double> tables = getTables();

            tables.forEach(rect -> {
                Prediction prediction = new Prediction(rect, page);
                processing.process(prediction);
                page.addTable(processing.getTable());
            });
        }

        chunks.clear();
        cells.clear();
    }

    private List<Rectangle2D.Double> getTables() {
        String parentPath = document.getSourceFile().toPath().getParent().getParent().toString();
        String name = document.getFileName().split(".pdf")[0];

        List<Rectangle2D.Double> tables = new ArrayList<>();

        try {
            setByKey(parentPath, name, "chunks");
            setByKey(parentPath, name, "cells");
            check();

            if (!chunks.isEmpty()) {
                double x1 = Collections.min(chunks, Comparator.comparing(Chunk::getX1)).x1;
                double y1 = Collections.min(chunks, Comparator.comparing(Chunk::getY1)).y1;
                double x2 = Collections.max(chunks, Comparator.comparing(Chunk::getX2)).x2;
                double y2 = Collections.max(chunks, Comparator.comparing(Chunk::getY2)).y2;

                tables.add(new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return tables;
    }

    private void setByKey(String parentPath, String name, String key) throws IOException {
        String path;
        boolean isChunks = key.equals("chunks");

        if (isChunks) {
            path = parentPath + "/chunk/" + name + ".chunk";
        } else {
            path = parentPath + "/structure/" + name + ".json";
        }

        String file = FileUtils.readFileToString(new File(path), "utf-8");
        JSONObject jsonObject = new JSONObject(file);
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
                StringBuilder stringBuilder = new StringBuilder();// = obj.getString("tex");
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
        String word = "";
        BreakIterator breakIterator = BreakIterator.getWordInstance();
        breakIterator.setText(text);
        int lastIndex = breakIterator.first();
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
