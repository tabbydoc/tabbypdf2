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
        List<TensorBox> tables = rcnnModel.detectTables(img);
        List<Rectangle2D> result = new ArrayList<Rectangle2D>();
        for (TensorBox tbox: tables) {
            Rectangle2D rect = new Rectangle2D.Float(
                    tbox.getMinX(),
                    tbox.getMinX(),
                    tbox.getMaxX(),
                    tbox.getMaxY());
            result.add(rect);
        }
        return result;
    }
}
