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
import java.util.ArrayList;

public class DataExtractor {
    private Document document;

    public void start(Document document) {
        if (document == null)
            return;

        this.document = document;

        ArrayList<Page> pages = (ArrayList<Page>) document.getPages();
        if (pages.isEmpty())
            return;

        PredictionProcessing processing = new PredictionProcessing(false);

        for (Page page : pages) {
            Rectangle2D.Double rect = getTables();

            if (rect == null)
                continue;

            Prediction prediction = new Prediction(rect, page);

            processing.process(prediction);
            page.addTable(processing.getTable());
        }
    }

    private Rectangle2D.Double getTables() {
        String pathParent = document.getSourceFile().toPath().getParent().getParent().toString().concat("/chunk/");
        String name = document.getFileName().split(".pdf")[0];
        String path = pathParent.concat(name).concat(".chunk");

        File file = new File(path);
        String content;
        try {
            content = FileUtils.readFileToString(file, "utf-8");
            JSONObject cellsJson = new JSONObject(content);
            JSONArray posArray = cellsJson.getJSONArray("chunks");

            double x1 = Double.MAX_VALUE;
            double y1 = Double.MAX_VALUE;
            double x2 = Double.MIN_VALUE;
            double y2 = Double.MIN_VALUE;

            for (int i = 0; i < posArray.length(); i++) {
                JSONObject obj = posArray.getJSONObject(i);
                JSONArray pos = obj.getJSONArray("pos");

                x1 = Math.min(x1, pos.getDouble(0));
                y1 = Math.min(y1, pos.getDouble(2));
                x2 = Math.max(x2, pos.getDouble(1));
                y2 = Math.max(y2, pos.getDouble(3));
            }

            return new Rectangle2D.Double(x1, y1, x2 - x1, y2 - y1);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


}
