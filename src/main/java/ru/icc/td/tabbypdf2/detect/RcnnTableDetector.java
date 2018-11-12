package ru.icc.td.tabbypdf2.detect;

import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;
import ru.icc.td.tabbypdf2.model.Page;
import ru.icc.td.tabbypdf2.util.Utils;

import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class RcnnTableDetector {

    AnnModel rcnnModel;

    public RcnnTableDetector(Path pathToModel, Path pathToLabelMap) throws Exception {
        rcnnModel = AnnModel.getInstance(pathToModel, pathToLabelMap);
    }

    public List<Rectangle2D> detectTables(BufferedImage img) throws IOException {
        float w = img.getWidth();
        float h = img.getHeight();
        List<TensorBox> tables = rcnnModel.detectTables(img);
        if (tables == null) {
            return null;
        }
        List<Rectangle2D> result = new ArrayList<Rectangle2D>();
        for (TensorBox tbox: tables) {
            if (tbox.getScore() > 0.95) {
                int ymin = Math.round(tbox.getMinY() * h);
                int xmin = Math.round(tbox.getMinX() * w);
                int ymax = Math.round(tbox.getMaxY() * h);
                int xmax = Math.round(tbox.getMaxX() * w);
                Rectangle2D rect = new Rectangle2D.Float(w - xmax, h - ymax, xmax - xmin, ymax - ymin);
                result.add(rect);
            }
        }
        return result;
    }
}
